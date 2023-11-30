package org.bobocode.hoverla.bring.web.servlet.processor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;
import org.bobocode.hoverla.bring.web.servlet.entity.ResponseEntity;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

import static java.util.Objects.nonNull;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.APPLICATION_JSON;
import static org.bobocode.hoverla.bring.web.util.TypeUtils.isTextPlainType;

/**
 * Processor for POJOs. It converts the return value to JSON and sets it as {@link HttpServletResponse}.
 *
 * @see org.bobocode.hoverla.bring.web.annotations.StatusCode
 * @see ResponseEntityReturnValueProcessor
 * @see TextPlainReturnValueProcessor
 */
public class PojoReturnValueProcessor extends AbstractReturnValueProcessor {

  public PojoReturnValueProcessor(List<HttpMessageConverter> converters) {
    super(converters);
  }

  @Override
  public boolean supports(Type type) {
    return nonNull(type) && !isTextPlainType(type) && ResponseEntity.class != type && hasJsonConverter(type);
  }

  @Override
  public boolean processReturnValue(Object returnValue,
                                    HandlerMethod handlerMethod,
                                    HttpServletResponse response) throws IOException {
    var converter = findConverter(returnValue.getClass(), APPLICATION_JSON.getValue());
    if (converter.isPresent()) {
      converter.get().write(returnValue, response, APPLICATION_JSON.getValue());

      setStatusCode(handlerMethod.getMethod(), response);

      return true;
    }

    return false;
  }

  private boolean hasJsonConverter(Type type) {
    return findConverter(type, APPLICATION_JSON.getValue()).isPresent();
  }

}
