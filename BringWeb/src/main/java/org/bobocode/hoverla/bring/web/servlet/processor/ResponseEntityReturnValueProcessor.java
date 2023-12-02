package org.bobocode.hoverla.bring.web.servlet.processor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;
import org.bobocode.hoverla.bring.web.servlet.entity.ResponseEntity;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

import lombok.extern.slf4j.Slf4j;

import static org.bobocode.hoverla.bring.web.util.TypeUtils.isResponseEntity;

/**
 * Processor that converts {@link ResponseEntity} into the appropriate servlet {@link HttpServletResponse response}.
 * Depending on the {@link ResponseEntity#getBody() body} the response content type can be either application/json or text/plain.
 *
 * @see ResponseEntity
 * @see org.bobocode.hoverla.bring.web.annotations.StatusCode
 * @see PojoReturnValueProcessor
 * @see TextPlainReturnValueProcessor
 */
@Slf4j
public class ResponseEntityReturnValueProcessor extends AbstractReturnValueProcessor {

  public ResponseEntityReturnValueProcessor(List<HttpMessageConverter> converters) {
    super(converters);
  }

  @Override
  public boolean supports(Type type) {
    return isResponseEntity(type);
  }

  @Override
  public boolean processReturnValue(Object returnValue,
                                    HandlerMethod handlerMethod,
                                    HttpServletResponse response) throws IOException {
    log.debug("Processing return value: {}", returnValue);

    if (returnValue instanceof ResponseEntity<?> responseEntity) {

      log.debug("Detected return value as ResponseEntity");

      var body = responseEntity.getBody();
      var contentType = getContentType(responseEntity);

      var converter = findConverter(body.getClass(), contentType);

      if (converter.isPresent()) {
        log.debug("Found converter for return value type: {}", body.getClass());

        setHeaders(responseEntity.getHeaders(), response);

        converter.get().write(body, response, contentType);

        response.setStatus(responseEntity.getStatus());

        return true;
      }
    }

    return false;
  }

  private String getContentType(ResponseEntity<?> responseEntity) {
    return Optional.ofNullable(responseEntity.getHeaders())
      .map(headers -> headers.get("Content-Type"))
      .flatMap(type -> type.stream().findFirst())
      .orElse(null);
  }

  private void setHeaders(Map<String, List<String>> headers, HttpServletResponse response) {
    headers.forEach((name, values) ->
                      values.forEach(value -> response.addHeader(name, value)));
  }

}
