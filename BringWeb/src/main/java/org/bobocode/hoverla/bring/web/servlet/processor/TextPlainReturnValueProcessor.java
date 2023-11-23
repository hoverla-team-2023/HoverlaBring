package org.bobocode.hoverla.bring.web.servlet.processor;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.TEXT_PLAIN;
import static org.bobocode.hoverla.bring.web.util.TypeUtils.isTextPlainType;

/**
 * Processor for that handles {@link String}, primitives and their wrapper types as a return value.
 */
public class TextPlainReturnValueProcessor extends AbstractReturnValueProcessor {

  public TextPlainReturnValueProcessor(List<HttpMessageConverter> converters) {
    super(converters);
  }

  @Override
  public boolean supports(Class<?> type) {
    return isTextPlainType(type);
  }

  @Override
  public boolean processReturnValue(Object returnValue,
                                    HandlerMethod handlerMethod,
                                    HttpServletResponse response) throws IOException {
    var contentType = TEXT_PLAIN.getValue();
    var converter = findConverter(returnValue.getClass(), contentType);

    if (converter.isPresent()) {
      // Write the response body to the output stream
      converter.get().write(returnValue, response, contentType);

      // Set response status code
      setStatusCode(handlerMethod.getMethod(), response);

      return true; // Successfully processed the return value
    }

    return false; // Unable to process the return value
  }

}