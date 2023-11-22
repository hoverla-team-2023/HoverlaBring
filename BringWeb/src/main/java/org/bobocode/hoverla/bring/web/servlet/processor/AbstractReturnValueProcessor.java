package org.bobocode.hoverla.bring.web.servlet.processor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.annotations.StatusCode;
import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;

import lombok.extern.log4j.Log4j2;

/**
 * Abstract base class for {@link ReturnValueProcessor} implementations. It looks for a suitable
 * {@link HttpMessageConverter converter} for the given return value type and content type.
 */
@Log4j2
public abstract class AbstractReturnValueProcessor implements ReturnValueProcessor {

  private final List<HttpMessageConverter> converters;

  protected AbstractReturnValueProcessor(List<HttpMessageConverter> converters) {
    this.converters = converters;
  }

  protected Optional<HttpMessageConverter> findConverter(Class<?> type, String contentType) {
    var converter = converters.stream()
      .filter(c -> c.canWrite(type, contentType))
      .findFirst();
    converter.ifPresent(c -> log.trace("Found HttpMessageConverter for type {} and content type {}: {}", type, contentType, c.getClass()));

    return converter;
  }

  protected void setStatusCode(Method method, HttpServletResponse response, int defaultStatusCode) {
    var statusCode = Optional.ofNullable(method.getAnnotation(StatusCode.class))
      .map(StatusCode::value)
      .orElse(defaultStatusCode);
    response.setStatus(statusCode);
  }

}
