package org.bobocode.hoverla.bring.web.servlet.converter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.exceptions.ObjectSerializingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.APPLICATION_JSON;
import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.TEXT_PLAIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonHttpMessageConverterTest {

  @InjectMocks
  private JsonHttpMessageConverter instance;
  @Mock
  private ObjectMapper objectMapper;

  @Test
  void givenApplicationJsonContentType_whenCanWrite_thenReturnTrue() {
    var result = instance.canWrite(Object.class, APPLICATION_JSON.getValue());
    assertTrue(result);
  }

  @Test
  void givenTextPlainContentType_whenCanWrite_thenReturnFalse() {
    var result = instance.canWrite(Object.class, "text/plain");
    assertFalse(result);
  }

  private record Pojo(String name) {}

  @Test
  void givenNoContentTypeAndSerializableReturnType_whenCanWrite_thenReturnTrue() {
    var type = Pojo.class;

    when(objectMapper.canSerialize(type)).thenReturn(true);

    var result = instance.canWrite(type, null);
    assertTrue(result);

    verify(objectMapper).canSerialize(type);
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
  void givenNoContentTypeAndPrimitiveOrStringReturnType_whenCanWrite_thenReturnFalse(Class<?> type) {
    var result = instance.canWrite(type, null);
    assertFalse(result);
  }

  @Test
  void givenProvidedContentType_whenWrite_thenWriteResponseWithProvidedContentType(
    @Mock HttpServletResponse response,
    @Mock PrintWriter writer
  ) throws IOException {
    var contentType = "my-application/json";
    var value = new Pojo("Bob");
    var json = "{\"name\": \"Bob\"}";

    when(response.getWriter()).thenReturn(writer);
    when(objectMapper.writeValueAsString(value)).thenReturn(json);

    instance.write(value, response, contentType);

    verify(response).setContentType(contentType);
    verify(writer).write(json);
  }

  @Test
  void givenNoContentType_whenWrite_thenSetApplicationJsonContentType(
    @Mock HttpServletResponse response,
    @Mock PrintWriter writer
  ) throws IOException {
    var value = new Pojo("Bob");
    var json = "{\"name\": \"Bob\"}";

    when(response.getWriter()).thenReturn(writer);
    when(objectMapper.writeValueAsString(value)).thenReturn(json);

    instance.write(value, response, null);

    verify(response).setContentType(APPLICATION_JSON.getValue());
    verify(writer).write(json);
  }

  @Test
  void givenJsonProcessingExceptionIsThrown_whenWrite_thenRethrowObjectSerializingException(@Mock HttpServletResponse response) throws IOException {
    var value = new Object();
    var message = "Failed to convert the return value to JSON and write the response. Verify the return value is POJO and can be serialized";
    var cause = new JsonParseException("Test message");

    when(objectMapper.writeValueAsString(value)).thenThrow(cause);

    var exception = assertThrows(ObjectSerializingException.class, () -> instance.write(value, response, null));

    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
  void givenApplicationJsonContentType_whenCanRead_thenReturnTrue() {
    var result = instance.canRead(Object.class, APPLICATION_JSON.getValue());
    assertTrue(result);
  }

  @Test
  void givenTextPlainContentType_whenCanRead_thenReturnFalse() {
    var result = instance.canRead(Object.class, "text/plain");
    assertFalse(result);
  }

  @Test
  void givenNoContentTypeAndSerializableReturnType_whenCanRead_thenReturnTrue(@Mock JavaType javaType) {
    var type = Pojo.class;

    when(objectMapper.constructType(type)).thenReturn(javaType);
    when(objectMapper.canDeserialize(javaType)).thenReturn(true);

    var result = instance.canRead(type, null);
    assertTrue(result);

    verify(objectMapper).constructType(type);
    verify(objectMapper).canDeserialize(javaType);
  }

  @ParameterizedTest
  @MethodSource("unsupportedTypes")
  void givenNoContentTypeAndPrimitiveOrStringReturnType_whenCanRead_thenReturnFalse(Class<?> type) {
    var result = instance.canRead(type, null);
    assertFalse(result);
  }

  @Test
  void givenType_whenRead_theConstructTypeAndReadValue(
    @Mock HttpServletRequest request,
    @Mock ServletInputStream inputStream,
    @Mock JavaType javaType
  ) throws IOException {
    var type = Pojo.class;

    when(request.getInputStream()).thenReturn(inputStream);
    when(objectMapper.constructType(type)).thenReturn(javaType);

    instance.read(type, request, null);

    verify(objectMapper).constructType(type);
    verify(objectMapper).readValue(inputStream, javaType);
  }

  @Test
  void givenApplicationJsonContentType_whenIsSupportedContentType_thenReturnsTrue() {
    var result = instance.isSupportedContentType(APPLICATION_JSON.getValue());
    assertTrue(result);
  }

  @Test
  void givenTextPlainContentType_whenIsSupportedContentType_thenReturnsFalse() {
    var result = instance.isSupportedContentType(TEXT_PLAIN.getValue());
    assertFalse(result);
  }

}