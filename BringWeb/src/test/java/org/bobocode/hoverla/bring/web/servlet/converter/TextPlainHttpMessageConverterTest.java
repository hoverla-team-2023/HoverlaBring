package org.bobocode.hoverla.bring.web.servlet.converter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.APPLICATION_JSON;
import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.TEXT_PLAIN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TextPlainHttpMessageConverterTest {

  private TextPlainHttpMessageConverter instance;
  @Mock
  private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    instance = new TextPlainHttpMessageConverter(objectMapper);
  }

  @Test
  void givenSupportedContentType_whenCanWrite_thenReturnsTrue() {
    var result = instance.canWrite(String.class, TEXT_PLAIN.getValue());
    assertTrue(result);
  }

  @Test
  void givenUnsupportedContentType_whenCanWrite_thenReturnsFalse() {
    var result = instance.canWrite(Object.class, APPLICATION_JSON.getValue());
    assertFalse(result);
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
  void givenNoSupportedContentTypeAndIsPrimitiveOrStringReturnType_whenCanWrite_thenReturnsTrue(Class<?> type) {
    var result = instance.canWrite(type, null);
    assertTrue(result);
  }

  @Test
  void givenNoSupportedContentTypeAndIsNotPrimitiveOrStringReturnType_whenCanWrite_thenReturnsFalse() {
    var result = instance.canWrite(Object.class, null);
    assertFalse(result);
  }

  @Test
  void givenContentType_whenWrite_thenWriteResponseWithProvidedContentType(
    @Mock HttpServletResponse response,
    @Mock PrintWriter writer
  ) throws IOException {
    String value = "Hello";
    var contentType = "my-text/plain";

    when(response.getWriter()).thenReturn(writer);

    instance.write(value, response, contentType);

    verify(response).setContentType(contentType);
    verify(writer).write(value);
  }

  @Test
  void givenContentTypeAbsent_whenWrite_thenWriteResponseWithTextPlainContentType(
    @Mock HttpServletResponse response,
    @Mock PrintWriter writer
  ) throws IOException {
    Integer value = 15;

    when(response.getWriter()).thenReturn(writer);

    instance.write(value, response, null);

    verify(response).setContentType(TEXT_PLAIN.getValue());
    verify(writer).write(value.toString());
  }

  @Test
  void givenSupportedContentType_whenCanRead_thenReturnsTrue() {
    var result = instance.canRead(String.class, TEXT_PLAIN.getValue());
    assertTrue(result);
  }

  @Test
  void givenUnsupportedContentType_whenCanRead_thenReturnsFalse() {
    var result = instance.canRead(Object.class, APPLICATION_JSON.getValue());
    assertFalse(result);
  }

  @ParameterizedTest
  @MethodSource("supportedTypes")
  void givenNoSupportedContentTypeAndIsPrimitiveOrStringReturnType_whenCanRead_thenReturnsTrue(Class<?> type) {
    var result = instance.canRead(type, null);
    assertTrue(result);
  }

  @Test
  void givenNoSupportedContentTypeAndIsNotPrimitiveOrStringReturnType_whenCanRead_thenReturnsFalse() {
    var result = instance.canRead(Object.class, null);
    assertFalse(result);
  }

  @Test
  void givenTextPlainContentType_whenIsSupportedContentType_thenReturnsTrue() {
    var result = instance.isSupportedContentType(TEXT_PLAIN.getValue());
    assertTrue(result);
  }

  @Test
  void givenApplicationJsonContentType_whenIsSupportedContentType_thenReturnsFalse() {
    var result = instance.isSupportedContentType(APPLICATION_JSON.getValue());
    assertFalse(result);
  }

}