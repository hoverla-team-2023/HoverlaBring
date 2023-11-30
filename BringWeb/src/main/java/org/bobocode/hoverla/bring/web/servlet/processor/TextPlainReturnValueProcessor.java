package org.bobocode.hoverla.bring.web.servlet.processor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

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
    // Set response status code
    setStatusCode(handlerMethod.getMethod(), response);

    if (isNull(returnValue)) {
      return true; // No return value to process so return true
    }

    var contentType = TEXT_PLAIN.getValue();
    var converter = findConverter(returnValue.getClass(), contentType);

    if (converter.isPresent()) {
      // Write the response body to the output stream
      converter.get().write(returnValue, response, contentType);

      return true; // Successfully processed the return value
    }

    return false; // Unable to process the return value
  }

}