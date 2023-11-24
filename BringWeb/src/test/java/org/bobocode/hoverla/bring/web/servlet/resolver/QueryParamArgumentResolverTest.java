package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.annotations.QueryParam;
import org.bobocode.hoverla.bring.web.exceptions.ObjectDeserializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCollection;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doReturn;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class QueryParamArgumentResolverTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  QueryParamArgumentResolver queryParamArgumentResolver = new QueryParamArgumentResolver();

  private static Stream<Arguments> primitiveOrWrappersOrEnumTestData() {
    return Stream.of(
      arguments(true, "fooBoolean"),
      arguments(false, "fooBoolean"),
      arguments(42, "fooInt"),
      arguments(((short) 5), "fooShort"),
      arguments(-123, "fooInt"),
      arguments(1234567890L, "fooLong"),
      arguments(3.14, "fooDouble"),
      arguments(-2.71828, "fooDouble"),
      arguments(1.23456789f, "fooFloat"),
      arguments(-0.98765432f, "fooFloat"),
      arguments("stringValue", "fooString"),
      arguments(true, "fooBooleanWrapper"),
      arguments(false, "fooBooleanWrapper"),
      arguments(42, "fooIntWrapper"),
      arguments(-123, "fooIntWrapper"),
      arguments(1234567890L, "fooLongWrapper"),
      arguments(3.14, "fooDoubleWrapper"),
      arguments(-2.71828, "fooDoubleWrapper"),
      arguments(1.23456789f, "fooFloatWrapper"),
      arguments(-0.98765432f, "fooFloatWrapper"),
      arguments(TestController.TestEnum.VALUE1, "fooEnum")
    );
  }

  @ParameterizedTest
  @MethodSource("primitiveOrWrappersOrEnumTestData")
  void givenPrimitiveOrWrappersQueryParam_whenResolve_thenVerifyReturnedValue(Object paramValue, String methodName) {
    Method methodToInvoke = Arrays.stream(TestController.class.getDeclaredMethods())
      .filter(method -> method.getName().equals(methodName))
      .findFirst().orElseThrow(() -> {
        Assertions.fail();
        return new IllegalArgumentException("Method not found: " + methodName);
      });
    Parameter parameter = methodToInvoke.getParameters()[0];
    doReturn(paramValue == null ? new String[] {} : new String[] { paramValue.toString() }).
      when(request).
      getParameterValues(parameter.getAnnotation(QueryParam.class).value());

    Object argument = queryParamArgumentResolver.resolveArgument(parameter, request, response);

    assertThat(argument).isEqualTo(paramValue);
    try {
      methodToInvoke.invoke(new TestController(), argument);
    } catch (IllegalAccessException | InvocationTargetException e) {
      Assertions.fail();
    }

  }

  @Test
  void givenMissingStringParam_whenResolve_thenReturnNull() {
    QueryParamArgumentResolver queryParamArgumentResolver = new QueryParamArgumentResolver();

    Parameter parameter = Arrays.stream(TestController.class.getDeclaredMethods())
      .filter(method -> method.getName().equals("fooString"))
      .findFirst()
      .map(method -> method.getParameters()[0])
      .orElseThrow(() -> {
        Assertions.fail();
        return new IllegalArgumentException("Method not found");
      });

    Object argument = queryParamArgumentResolver.resolveArgument(parameter, request, response);

    assertThat(argument).isNull();
  }

  private static Stream<Arguments> collectionTestData() {
    return Stream.of(
      arguments("fooStringList", List.of("John", "Doe"), "strings", new String[] { "John,Doe" }),
      arguments("fooListEnum", List.of(TestController.TestEnum.VALUE1, TestController.TestEnum.VALUE2), "enums",
                new String[] { "VALUE1", "VALUE2" }),
      arguments("fooSetInt", Set.of(1, 2, 3), "numbers", new String[] { "1", "2", "3" }),
      arguments("fooSetInt", null, "numbers", new String[] {}),
      arguments("fooMap", null, "map", new String[] { "somevalue" })
    );
  }

  @ParameterizedTest
  @MethodSource("collectionTestData")
  void givenCollectionQueryParam_whenResolve_thenVerifyReturnedValue(String methodName, Object expectedValue, String name, String[] values) {
    Method methodToInvoke = Arrays.stream(TestController.class.getDeclaredMethods())
      .filter(method -> method.getName().equals(methodName))
      .findFirst().orElseThrow(() -> {
        Assertions.fail();
        return new IllegalArgumentException("Method not found: " + methodName);
      });
    Parameter parameter = methodToInvoke.getParameters()[0];
    doReturn(values).
      when(request).
      getParameterValues(name);

    assertThat(queryParamArgumentResolver.supportsParameter(parameter)).isEqualTo(true);
    Object argument = queryParamArgumentResolver.resolveArgument(parameter, request, response);

    if (Collection.class.isAssignableFrom(parameter.getType())) {
      assertThatCollection((Collection<?>) argument).isEqualTo(expectedValue);
    } else {
      assertThat(argument).isEqualTo(null);
    }

    try {
      methodToInvoke.invoke(new TestController(), argument);
    } catch (IllegalAccessException | InvocationTargetException e) {
      Assertions.fail();
    }
  }

  private static Stream<Arguments> exceptionTestData() {
    return Stream.of(
      //wrong enum value
      arguments("fooListEnum", "enums", new String[] { "VALUE1", "WRONG_ENUM_VALUE" }),
      //missing primitive number value
      arguments("fooInt", "param", new String[] {}));
  }

  @ParameterizedTest
  @MethodSource("exceptionTestData")
  void givenNotValidQueryParam_whenResolve_thenThrowException(String methodName, String name, String[] values) {
    Method methodToInvoke = Arrays.stream(TestController.class.getDeclaredMethods())
      .filter(method -> method.getName().equals(methodName))
      .findFirst().orElseThrow(() -> {
        Assertions.fail();
        return new IllegalArgumentException("Method not found: " + methodName);
      });
    Parameter parameter = methodToInvoke.getParameters()[0];
    doReturn(values).
      when(request).
      getParameterValues(name);

    assertThatThrownBy(() -> {queryParamArgumentResolver.resolveArgument(parameter, request, response);})
      .isInstanceOf(ObjectDeserializationException.class);
  }

  private static class TestController {

    void fooListString(@QueryParam("names") List<String> names) {
    }

    void unsupportedParam(@QueryParam("param") CustomClass customClass) {
    }

    void fooBoolean(@QueryParam("param") boolean param) {
    }

    void fooShort(@QueryParam("param") short param) {
    }

    void fooInt(@QueryParam("param") int param) {
    }

    void fooLong(@QueryParam("param") long param) {
    }

    void fooDouble(@QueryParam("param") double param) {
    }

    void fooFloat(@QueryParam("param") float param) {
    }

    void fooString(@QueryParam("param") String param) {

    }

    // Wrapper types
    void fooBooleanWrapper(@QueryParam("param") Boolean param) {
    }

    void fooIntWrapper(@QueryParam("param") Integer param) {
    }

    void fooLongWrapper(@QueryParam("param") Long param) {
    }

    void fooDoubleWrapper(@QueryParam("param") Double param) {
    }

    void fooFloatWrapper(@QueryParam("param") Float param) {
    }

    void fooEnum(@QueryParam("param") TestEnum param) {
    }

    void fooListEnum(@QueryParam("enums") ArrayList<TestEnum> values) {
    }

    void fooSetInt(@QueryParam("numbers") Set<Integer> numbers) {
    }

    void fooStringList(@QueryParam("strings") List<String> strings) {
    }

    void fooMap(@QueryParam("map") Map<?, ?> map) {
    }

    private enum TestEnum {
      VALUE1,
      VALUE2,
      VALUE3
    }

  }

  private static class CustomClass {
  }

}

