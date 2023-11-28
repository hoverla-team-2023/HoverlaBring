package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;

import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;
import org.bobocode.hoverla.bring.web.servlet.entity.RequestEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.APPLICATION_JSON;
import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.TEXT_PLAIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestEntityMethodArgumentResolverTest {

  private RequestEntityMethodArgumentResolver instance;

  @Mock
  private HttpMessageConverter messageConverter;

  @BeforeEach
  void setup() {
    instance = new RequestEntityMethodArgumentResolver(List.of(messageConverter));
  }

  @Test
  void givenRequestEntityParameter_whenSupportsParameter_thenReturnTrue() throws NoSuchMethodException {
    var parameter = this.getClass().getDeclaredMethod("mockMethod", RequestEntity.class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertTrue(result);
  }

  @Test
  void givenTestDtoParameter_whenSupportsParameter_thenReturnFalse() throws NoSuchMethodException {
    var parameter = this.getClass().getDeclaredMethod("mockMethod", TestDto.class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertFalse(result);
  }

  private record TestDto() {}

  private void mockMethod(TestDto parameter) {}

  private void mockMethod(RequestEntity<TestDto> parameter) {}

  private void mockMethodRawGeneric(RequestEntity parameter) {}

  static Stream<Arguments> resolveArgumentParameters() {
    return Stream.of(
      arguments("mockMethod", TestDto.class),
      arguments("mockMethodRawGeneric", Object.class)
    );
  }

  @ParameterizedTest
  @MethodSource("resolveArgumentParameters")
  void givenMessageConverterPresent_whenResolveArgument_thenReturnResult(
    String mockMethod,
    Type genericType,
    @Mock HttpServletRequest request
  ) throws IOException, NoSuchMethodException {
    var contentType = APPLICATION_JSON.getValue();
    var parameter = this.getClass().getDeclaredMethod(mockMethod, RequestEntity.class).getParameters()[0];
    var expected = new TestDto();

    var expectedHeaders = Map.of(
      "Content-Type", List.of(contentType),
      "Accept", List.of(contentType, TEXT_PLAIN.getValue())
    );
    var headers = createVector("Content-Type", "Accept");
    var contentTypeHeader = createVector(contentType);
    var acceptHeader = createVector(contentType, TEXT_PLAIN.getValue());

    when(request.getContentType()).thenReturn(contentType);
    when(request.getHeaderNames()).thenReturn(headers.elements());
    when(request.getHeaders("Content-Type")).thenReturn(contentTypeHeader.elements());
    when(request.getHeaders("Accept")).thenReturn(acceptHeader.elements());
    when(messageConverter.canRead(genericType, contentType)).thenReturn(true);
    when(messageConverter.read(genericType, request, contentType)).thenReturn(expected);

    var actual = instance.resolveArgument(null, parameter, request, null);

    assertTrue(RequestEntity.class.isAssignableFrom(actual.getClass()));
    var actualRequestEntity = (RequestEntity<?>) actual;
    assertEquals(expected, actualRequestEntity.getBody());
    assertEquals(expectedHeaders, actualRequestEntity.getHeaders());

    verify(request).getContentType();
    verify(request).getHeaderNames();
    verify(request).getHeaders("Content-Type");
    verify(request).getHeaders("Accept");
    verify(messageConverter).canRead(genericType, contentType);
    verify(messageConverter).read(genericType, request, contentType);
  }

  private Vector<String> createVector(String... values) {
    var vector = new Vector<String>(values.length);
    vector.addAll(Arrays.asList(values));
    return vector;
  }

}