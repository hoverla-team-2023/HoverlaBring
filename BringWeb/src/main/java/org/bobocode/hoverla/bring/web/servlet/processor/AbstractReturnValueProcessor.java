package org.bobocode.hoverla.bring.web.servlet.processor;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.annotations.StatusCode;
import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;

import lombok.extern.slf4j.Slf4j;

/**
 * Abstract base class for {@link ReturnValueProcessor} implementations. It looks for a suitable
 * {@link HttpMessageConverter converter} for the given return value type and content type.
 */
@Slf4j
public abstract class AbstractReturnValueProcessor implements ReturnValueProcessor {

  protected static final int DEFAULT_STATUS_CODE = 200;
  private final List<HttpMessageConverter> converters;

  protected AbstractReturnValueProcessor(List<HttpMessageConverter> converters) {
    this.converters = converters;
  }

  protected Optional<HttpMessageConverter> findConverter(Type type, String contentType) {
    var converter = converters.stream()
      .filter(c -> c.canWrite(type, contentType))
      .findFirst();
    converter.ifPresent(c -> log.trace("Found HttpMessageConverter for type {} and content type {}: {}", type, contentType, c.getClass()));

    return converter;
  }

  protected void setStatusCode(Method method, HttpServletResponse response) {
    var statusCode = Optional.ofNullable(method.getAnnotation(StatusCode.class))
      .map(StatusCode::value)
      .orElse(DEFAULT_STATUS_CODE);
    response.setStatus(statusCode);
  }

}
