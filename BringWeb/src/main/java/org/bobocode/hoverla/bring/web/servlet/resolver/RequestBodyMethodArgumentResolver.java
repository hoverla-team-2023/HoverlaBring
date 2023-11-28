package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.lang.reflect.Parameter;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.annotations.RequestBody;
import org.bobocode.hoverla.bring.web.exceptions.InvalidContentTypeException;
import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;

import lombok.extern.log4j.Log4j2;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.APPLICATION_JSON;

/**
 * {@link HandlerMethodArgumentResolver} that handles {@link RequestBody} parameters. Only <code>Content-Type: application/json</code> is supported.
 */
@Log4j2
public class RequestBodyMethodArgumentResolver extends DelegatingHttpMessageConverterArgumentResolver {

  public RequestBodyMethodArgumentResolver(List<HttpMessageConverter> messageConverters) {
    super(messageConverters);
  }

  @Override
  public boolean supportsParameter(Parameter parameter) {
    return parameter.isAnnotationPresent(RequestBody.class);
  }

  @Override
  public Object resolveArgument(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
    var contentType = request.getContentType();
    verifyContentType(contentType);

    var type = parameter.getParameterizedType();
    log.debug("Converting request body to {}", type.getTypeName());

    return readRequestBody(type, request, contentType);
  }

  private void verifyContentType(String contentType) {
    if (!APPLICATION_JSON.getValue().equals(contentType)) {
      var message = "Unsupported content type: %s. Only %s is supported.".formatted(contentType, APPLICATION_JSON.getValue());

      log.error(message);
      throw new InvalidContentTypeException(message);
    }
  }

}
