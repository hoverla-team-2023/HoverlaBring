package org.bobocode.hoverla.bring.web.servlet.processor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.annotations.StatusCode;
import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;
import org.bobocode.hoverla.bring.web.servlet.entity.ResponseEntity;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PojoReturnValueProcessorTest {

  private PojoReturnValueProcessor instance;
  @Mock
  private HttpMessageConverter converter;

  @BeforeEach
  void setup() {
    instance = new PojoReturnValueProcessor(List.of(converter));
  }

  private record Pojo() {}

  @Test
  void givenHasJsonConverter_whenSupports_thenReturnTrue() {
    var type = Pojo.class;
    var contentType = APPLICATION_JSON.getValue();

    when(converter.canWrite(type, contentType)).thenReturn(true);

    var result = instance.supports(type);

    assertTrue(result);
    verify(converter).canWrite(type, contentType);
  }

  static Stream<Arguments> unsupportedTypes() {
    return Stream.of(
      arguments(String.class),
      arguments(Integer.class),
      arguments(int.class),
      arguments(Double.class),
      arguments(double.class),
      arguments(Long.class),
      arguments(long.class),
      arguments(Boolean.class),
      arguments(boolean.class),
      arguments(Character.class),
      arguments(char.class),
      arguments(Void.class)
    );
  }

  @ParameterizedTest
  @MethodSource("unsupportedTypes")
  void givenPrimitiveTypeOrString_whenSupports_thenReturnFalse(Class<?> type) {
    var result = instance.supports(type);

    assertFalse(result);
    verifyNoInteractions(converter);
  }

  @Test
  void givenResponseEntity_whenSupports_thenReturnFalse() {
    var result = instance.supports(ResponseEntity.class);

    assertFalse(result);
    verifyNoInteractions(converter);
  }

  private void mockMethod() {}

  @Test
  void givenPojoReturnValue_whenProcessReturnValue_thenWriteToResponseAndReturnTrue(
    @Mock HandlerMethod handlerMethod,
    @Mock HttpServletResponse response
  ) throws IOException, NoSuchMethodException {
    var contentType = "application/json";
    var returnValue = new Pojo();
    var mockMethod = this.getClass().getDeclaredMethod("mockMethod");

    when(converter.canWrite(Pojo.class, contentType)).thenReturn(true);
    when(handlerMethod.getMethod()).thenReturn(mockMethod);

    var result = instance.processReturnValue(returnValue, handlerMethod, response);

    assertTrue(result);
    verify(converter).canWrite(Pojo.class, contentType);
    verify(converter).write(returnValue, response, contentType);
    verify(response).setStatus(200);
  }

  @StatusCode(304)
  void mockMethodWithStatusCode() {}

  @Test
  void givenMethodWithStatusCodeAnnotation_whenProcessReturnValue_thenWriteToResponseAndReturnTrue(
    @Mock HandlerMethod handlerMethod,
    @Mock HttpServletResponse response
  ) throws IOException, NoSuchMethodException {
    var contentType = "application/json";
    var statusCode = 304;
    var returnValue = new Pojo();
    var mockMethod = this.getClass().getDeclaredMethod("mockMethodWithStatusCode");

    when(converter.canWrite(Pojo.class, contentType)).thenReturn(true);
    when(handlerMethod.getMethod()).thenReturn(mockMethod);

    var result = instance.processReturnValue(returnValue, handlerMethod, response);

    assertTrue(result);
    verify(response).setStatus(statusCode);
  }

  @Test
  void givenNotSupportedReturnValue_whenProcessReturnValue_thenDoNothingAndReturnFalse(
    @Mock HandlerMethod handlerMethod,
    @Mock HttpServletResponse response
  ) throws IOException {
    var returnValue = new Object();
    var contentType = APPLICATION_JSON.getValue();

    when(converter.canWrite(Object.class, contentType)).thenReturn(false);

    var result = instance.processReturnValue(returnValue, handlerMethod, response);

    assertFalse(result);
    verify(converter).canWrite(Object.class, contentType);
    verifyNoInteractions(handlerMethod, response);
  }

}