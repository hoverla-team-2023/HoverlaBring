package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.bobocode.hoverla.bring.web.annotations.RequestBody;
import org.bobocode.hoverla.bring.web.exceptions.InvalidContentTypeException;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestBodyMethodArgumentResolverTest {

  private RequestBodyMethodArgumentResolver instance;

  @Mock
  private HttpMessageConverter messageConverter;

  @BeforeEach
  void setup() {
    instance = new RequestBodyMethodArgumentResolver(List.of(messageConverter));
  }

  @Test
  void givenParameterWithRequestBodyAnnotation_whenSupportsParameter_thenReturnTrue() throws NoSuchMethodException {
    var parameter = this.getClass().getDeclaredMethod("mockMethod", List.class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertTrue(result);
  }

  @Test
  void givenParameterWithoutRequestBodyAnnotation_whenSupportsParameter_thenReturnFalse() throws NoSuchMethodException {
    var parameter = this.getClass().getDeclaredMethod("mockMethodWithoutRequestBody", Object.class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertFalse(result);
  }

  void mockMethod(@RequestBody List<Object> parameter) {}

  void mockMethodWithoutRequestBody(Object parameter) {}

  @Test
  void givenMessageConverterPresent_whenResolveArgument_thenReturnResult(@Mock HttpServletRequest request) throws IOException, NoSuchMethodException {
    var contentType = APPLICATION_JSON.getValue();
    var parameter = getClass().getDeclaredMethod("mockMethod", List.class).getParameters()[0];
    var type = parameter.getParameterizedType();
    var expected = new Object();

    when(request.getContentType()).thenReturn(contentType);
    when(messageConverter.canRead(type, contentType)).thenReturn(true);
    when(messageConverter.read(type, request, contentType)).thenReturn(expected);

    var actual = instance.resolveArgument(null, parameter, request, null);
    assertEquals(expected, actual);

    verify(messageConverter).canRead(type, contentType);
    verify(messageConverter).read(type, request, contentType);
  }

  @Test
  void givenNoMessageConverterAvailable_whenResolveArgument_thenThrowMessageConverterNotFoundException(
    @Mock HttpServletRequest request
  ) throws NoSuchMethodException, IOException {
    var contentType = APPLICATION_JSON.getValue();
    var parameter = getClass().getDeclaredMethod("mockMethod", List.class).getParameters()[0];
    var type = parameter.getParameterizedType();

    when(request.getContentType()).thenReturn(contentType);
    when(messageConverter.canRead(type, contentType)).thenReturn(false);

    assertThrows(MessageConverterNotFoundException.class, () -> instance.resolveArgument(null, parameter, request, null));

    verify(messageConverter).canRead(type, contentType);
    verify(messageConverter, never()).read(any(), any(), any());
  }

  @Test
  void givenMessageConverterThrowsIOException_whenResolveArgument_thenThrowObjectDeserializationException(
    @Mock HttpServletRequest request
  ) throws IOException, NoSuchMethodException {
    var contentType = APPLICATION_JSON.getValue();
    var parameter = getClass().getDeclaredMethod("mockMethod", List.class).getParameters()[0];
    var type = parameter.getParameterizedType();

    when(request.getContentType()).thenReturn(contentType);
    when(messageConverter.canRead(type, contentType)).thenReturn(true);
    when(messageConverter.read(type, request, contentType)).thenThrow(IOException.class);

    assertThrows(ObjectDeserializationException.class, () -> instance.resolveArgument(null, parameter, request, null));

    verify(messageConverter).canRead(type, contentType);
    verify(messageConverter).read(type, request, contentType);
  }

  @Test
  void givenNoContentType_whenResolveArgument_thenThrowInvalidContentTypeException(@Mock HttpServletRequest request) throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", List.class).getParameters()[0];

    assertThrows(InvalidContentTypeException.class, () -> instance.resolveArgument(null, parameter, request, null));

    verifyNoInteractions(messageConverter);
  }

}