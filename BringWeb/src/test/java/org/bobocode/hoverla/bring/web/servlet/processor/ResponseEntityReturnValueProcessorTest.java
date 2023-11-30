package org.bobocode.hoverla.bring.web.servlet.processor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletResponse;

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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResponseEntityReturnValueProcessorTest {

  private ResponseEntityReturnValueProcessor instance;
  @Mock
  private HttpMessageConverter httpMessageConverter;

  @BeforeEach
  void setup() {
    instance = new ResponseEntityReturnValueProcessor(List.of(httpMessageConverter));
  }

  @Test
  void givenResponseEntityType_whenSupports_thenReturnTrue() {
    var result = instance.supports(ResponseEntity.class);
    assertTrue(result);
  }

  static Stream<Arguments> unsupportedTypes() {
    return Stream.of(
      arguments(Object.class),
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
  void givenNotResponseEntityType_whenSupports_thenReturnFalse(Class<?> type) {
    var result = instance.supports(type);
    assertFalse(result);
  }

  @Test
  void givenResponseEntityReturnValue_whenProcessReturnValue_thenWriteToResponseAndReturnTrue(
    @Mock HandlerMethod handlerMethod,
    @Mock HttpServletResponse response
  ) throws IOException {
    var header = "Content-Type";
    var contentType = "application/json";
    Object body = new Object();
    var status = 201;
    var returnValue = new ResponseEntity<>(body, Map.of(header, List.of(contentType)), status);

    when(httpMessageConverter.canWrite(Object.class, contentType)).thenReturn(true);

    var result = instance.processReturnValue(returnValue, handlerMethod, response);

    assertTrue(result);
    verify(httpMessageConverter).canWrite(Object.class, contentType);
    verify(response).addHeader(header, contentType);
    verify(httpMessageConverter).write(body, response, contentType);
    verify(response).setStatus(status);
  }

  @Test
  void givenNotResponseEntityReturnValue_whenProcessReturnValue_thenDoNothingAndReturnFalse(
    @Mock HandlerMethod handlerMethod,
    @Mock HttpServletResponse response
  ) throws IOException {
    var returnValue = new Object();

    var result = instance.processReturnValue(returnValue, handlerMethod, response);

    assertFalse(result);
    verifyNoInteractions(handlerMethod, response);
  }

}