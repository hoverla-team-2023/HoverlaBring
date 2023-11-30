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

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.TEXT_PLAIN;
import static org.bobocode.hoverla.bring.web.servlet.processor.AbstractReturnValueProcessor.DEFAULT_STATUS_CODE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TextPlainReturnValueProcessorTest {

  private TextPlainReturnValueProcessor instance;
  @Mock
  private HttpMessageConverter converter;

  @BeforeEach
  void setup() {
    instance = new TextPlainReturnValueProcessor(List.of(converter));
  }

  static Stream<Arguments> supportedTypes() {
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
      arguments(char.class)
    );
  }

  @ParameterizedTest
  @MethodSource("supportedTypes")
  void givePrimitiveOrStringType_whenSupports_thenReturnsTrue(Class<?> type) {
    var result = instance.supports(type);
    assertTrue(result);
  }

  @Test
  void givenResponseEntity_whenSupports_thenReturnsFalse() {
    var result = instance.supports(ResponseEntity.class);
    assertFalse(result);
  }

  private record Pojo() {}

  @Test
  void givenPojoType_whenSupports_thenReturnsTrue() {
    var result = instance.supports(Pojo.class);
    assertFalse(result);
  }

  @Test
  void givenVoidType_whenSupports_thenReturnsTrue() {
    var result = instance.supports(Void.TYPE);
    assertTrue(result);
  }

  @Test
  void givenNull_whenSupports_thenReturnsFalse() {
    var result = instance.supports(null);
    assertFalse(result);
  }

  private void mockMethod() {}

  @Test
  void givenPrimitiveReturnValue_whenProcessReturnValue_thenWriteToResponseAndReturnTrue(
    @Mock HandlerMethod handlerMethod,
    @Mock HttpServletResponse response
  ) throws IOException, NoSuchMethodException {
    var contentType = TEXT_PLAIN.getValue();
    int returnValue = 20;
    var mockMethod = this.getClass().getDeclaredMethod("mockMethod");

    when(converter.canWrite(Integer.class, contentType)).thenReturn(true);
    when(handlerMethod.getMethod()).thenReturn(mockMethod);

    var result = instance.processReturnValue(returnValue, handlerMethod, response);

    assertTrue(result);
    verify(converter).canWrite(Integer.class, contentType);
    verify(converter).write(returnValue, response, contentType);
    verify(response).setStatus(200);
  }

  @Test
  void givenStringReturnValue_whenProcessReturnValue_thenWriteToResponseAndReturnTrue(
    @Mock HandlerMethod handlerMethod,
    @Mock HttpServletResponse response
  ) throws IOException, NoSuchMethodException {
    var contentType = TEXT_PLAIN.getValue();
    String returnValue = "20";
    var mockMethod = this.getClass().getDeclaredMethod("mockMethod");

    when(converter.canWrite(String.class, contentType)).thenReturn(true);
    when(handlerMethod.getMethod()).thenReturn(mockMethod);

    var result = instance.processReturnValue(returnValue, handlerMethod, response);

    assertTrue(result);
    verify(converter).canWrite(String.class, contentType);
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
    var contentType = TEXT_PLAIN.getValue();
    var statusCode = 304;
    var returnValue = 50;
    var mockMethod = this.getClass().getDeclaredMethod("mockMethodWithStatusCode");

    when(converter.canWrite(Integer.class, contentType)).thenReturn(true);
    when(handlerMethod.getMethod()).thenReturn(mockMethod);

    var result = instance.processReturnValue(returnValue, handlerMethod, response);

    assertTrue(result);
    verify(response).setStatus(statusCode);
  }

  @Test
  void givenNullReturnValue_whenProcessReturnValue_thenSetStatusCode(
    @Mock HandlerMethod handlerMethod,
    @Mock HttpServletResponse response
  ) throws NoSuchMethodException, IOException {
    var statusCode = 304;
    var mockMethod = this.getClass().getDeclaredMethod("mockMethodWithStatusCode");

    when(handlerMethod.getMethod()).thenReturn(mockMethod);

    var result = instance.processReturnValue(null, handlerMethod, response);

    assertTrue(result);
    verify(response).setStatus(statusCode);
    verifyNoInteractions(converter);
  }

  @Test
  void givenNotSupportedReturnValue_whenProcessReturnValue_thenDoNothingAndReturnFalse(
    @Mock HandlerMethod handlerMethod,
    @Mock HttpServletResponse response
  ) throws IOException, NoSuchMethodException {
    var returnValue = new Object();
    var contentType = TEXT_PLAIN.getValue();
    var mockMethod = this.getClass().getDeclaredMethod("mockMethod");

    when(converter.canWrite(Object.class, contentType)).thenReturn(false);
    when(handlerMethod.getMethod()).thenReturn(mockMethod);

    var result = instance.processReturnValue(returnValue, handlerMethod, response);

    assertFalse(result);
    verify(converter).canWrite(Object.class, contentType);
    verify(handlerMethod).getMethod();
    verify(response).setStatus(DEFAULT_STATUS_CODE);
  }

}