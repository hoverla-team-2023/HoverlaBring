package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.bobocode.hoverla.bring.web.exceptions.ObjectDeserializationException;
import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract base class for {@link HandlerMethodArgumentResolver} implementations that delegate conversion to {@link HttpMessageConverter}
 * <p>Extend this class if you need to deserialize JSON request body to the required {@link Type}</p>
 */
@Slf4j
@RequiredArgsConstructor
public abstract class DelegatingHttpMessageConverterArgumentResolver implements HandlerMethodArgumentResolver {

  private final List<HttpMessageConverter> messageConverters;

  protected Object readRequestBody(Type type, HttpServletRequest request, String contentType) {
    var messageConverter = findConverter(type, request.getContentType());

    try {
      return messageConverter.read(type, request, contentType);
    } catch (IOException e) {
      var message = "Failed to convert the request body to the required type %s".formatted(type.getTypeName());

      log.error(message, e);
      throw new ObjectDeserializationException(message, e);
    }
  }

  private HttpMessageConverter findConverter(Type type, String contentType) {
    return messageConverters.stream()
      .filter(converter -> converter.canRead(type, contentType))
      .findFirst()
      .orElseThrow(() -> new RuntimeException("No message converter found for " + type.getTypeName()));
  }

}
