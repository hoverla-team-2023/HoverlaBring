package org.bobocode.hoverla.bring.web.servlet.processor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.TEXT_PLAIN;
import static org.bobocode.hoverla.bring.web.util.TypeUtils.isTextPlainType;

/**
 * Processor for that handles {@link String}, {@link Void}, primitives and their wrapper types as a return value.
 *
 * @see org.bobocode.hoverla.bring.web.annotations.StatusCode
 * @see ResponseEntityReturnValueProcessor
 * @see PojoReturnValueProcessor
 */
@Slf4j
public class TextPlainReturnValueProcessor extends AbstractReturnValueProcessor {

  public TextPlainReturnValueProcessor(List<HttpMessageConverter> converters) {
    super(converters);
  }

  @Override
  public boolean supports(Type type) {
    return nonNull(type) && isTextPlainType(type);
  }

  @Override
  public boolean processReturnValue(Object returnValue,
                                    HandlerMethod handlerMethod,
                                    HttpServletResponse response) throws IOException {
    setStatusCode(handlerMethod.getMethod(), response);

    if (isNull(returnValue)) {

      log.debug("No return value to process, returning true.");

      return true;
    }

    var contentType = TEXT_PLAIN.getValue();
    var converter = findConverter(returnValue.getClass(), contentType);

    if (converter.isPresent()) {

      log.debug("Found converter for return value type: {}", returnValue.getClass());

      converter.get().write(returnValue, response, contentType);

      log.debug("Processed return value successfully.");

      return true;
    }

    log.debug("Unable to process the return value, returning false.");
    return false;
  }

}