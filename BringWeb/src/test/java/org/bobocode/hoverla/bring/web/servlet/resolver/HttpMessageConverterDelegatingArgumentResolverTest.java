package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.exceptions.MessageConverterNotFoundException;
import org.bobocode.hoverla.bring.web.exceptions.ObjectDeserializationException;
import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpMessageConverterDelegatingArgumentResolverTest {

  private HttpMessageConverterDelegatingArgumentResolver instance;

  @Mock
  private HttpMessageConverter messageConverter;

  private static class MockHttpMessageConverterDelegatingArgumentResolver extends HttpMessageConverterDelegatingArgumentResolver {

    public MockHttpMessageConverterDelegatingArgumentResolver(List<HttpMessageConverter> messageConverters) {
      super(messageConverters);
    }

    @Override
    public boolean supportsParameter(Parameter parameter) {
      return false;
    }

    @Override
    public Object resolveArgument(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
      return null;
    }

  }

  @BeforeEach
  void setup() {
    instance = new MockHttpMessageConverterDelegatingArgumentResolver(List.of(messageConverter));
  }

  @Test
  void givenMessageConverterPresent_whenReadRequestBody_thenReturnResult(@Mock HttpServletRequest request) throws IOException {
    var type = Object.class;
    var contentType = APPLICATION_JSON.getValue();
    var expected = new Object();

    when(request.getContentType()).thenReturn(contentType);
    when(messageConverter.canRead(type, contentType)).thenReturn(true);
    when(messageConverter.read(type, request, contentType)).thenReturn(expected);

    var actual = instance.readRequestBody(type, request);
    assertEquals(expected, actual);

    verify(request).getContentType();
    verify(messageConverter).canRead(type, contentType);
    verify(messageConverter).read(type, request, contentType);
  }

  @Test
  void givenNoMessageConverterAvailable_whenReadRequestBody_thenThrowMessageConverterNotFoundException(@Mock HttpServletRequest request) throws IOException {
    var type = Object.class;
    var contentType = APPLICATION_JSON.getValue();
    var expected = "No message converter found for type %s, content-type: %s".formatted(type.getTypeName(), contentType);

    when(request.getContentType()).thenReturn(contentType);
    when(messageConverter.canRead(type, contentType)).thenReturn(false);

    var actual = assertThrows(MessageConverterNotFoundException.class, () -> instance.readRequestBody(type, request));
    assertEquals(expected, actual.getMessage());

    verify(request).getContentType();
    verify(messageConverter).canRead(type, contentType);
    verify(messageConverter, never()).read(any(), any(), any());
  }

  @Test
  void givenMessageConverterThrowsIOException_whenWriteRequestBody_thenThrowObjectDeserializationException(@Mock HttpServletRequest request) throws IOException {
    var type = Object.class;
    var contentType = APPLICATION_JSON.getValue();
    var expected = "Failed to convert the request body to the required type %s".formatted(type.getTypeName());

    when(request.getContentType()).thenReturn(contentType);
    when(messageConverter.canRead(type, contentType)).thenReturn(true);
    when(messageConverter.read(type, request, contentType)).thenThrow(IOException.class);

    var actual = assertThrows(ObjectDeserializationException.class, () -> instance.readRequestBody(type, request));
    assertEquals(expected, actual.getMessage());

    verify(request).getContentType();
    verify(messageConverter).canRead(type, contentType);
    verify(messageConverter).read(type, request, contentType);
  }

}