package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.io.IOException;
import java.lang.reflect.Parameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.annotations.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.APPLICATION_JSON;

/**
 * Handles {@link RequestBody}
 */
@Log4j2
@RequiredArgsConstructor
public class RequestBodyMethodArgumentResolver implements HandlerMethodArgumentResolver {

  private final ObjectMapper objectMapper;

  @Override
  public boolean supportsParameter(Parameter parameter) {
    return parameter.isAnnotationPresent(RequestBody.class);
  }

  @Override
  public Object resolveArgument(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
    verifyContentType(request);

    var type = parameter.getType();
    try {
      return objectMapper.readValue(request.getReader(), type);
    } catch (IOException e) {
      var message = "Failed to convert the request body to the required type %s".formatted(type.getSimpleName());
      log.error(message, e);

      // TODO: 23.11.2023 throw a dedicated exception and log it (ObjectDeserializationException)
      throw new RuntimeException(message, e);
    }
  }

  private void verifyContentType(HttpServletRequest request) {
    var contentType = request.getContentType();
    if (!APPLICATION_JSON.getValue().equals(contentType)) {
      var message = "Unsupported content type: %s. Only %s is supported.".formatted(contentType, APPLICATION_JSON.getValue());
      log.error(message);

      // TODO: 23.11.2023 throw a dedicated exception and log it
      throw new RuntimeException(message);
    }
  }

}
