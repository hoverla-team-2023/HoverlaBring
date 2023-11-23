package org.bobocode.hoverla.bring.web.servlet.converter;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.exceptions.ObjectSerializingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.APPLICATION_JSON;
import static org.bobocode.hoverla.bring.web.util.TypeUtils.isTextPlainType;

/**
 * Message converter for JSON responses. It converts the response body into a JSON string and
 * sets the Content-Type header to "application/json".
 */
@Log4j2
public class JsonHttpMessageConverter implements HttpMessageConverter {

  private final ObjectMapper objectMapper;

  public JsonHttpMessageConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public boolean canWrite(Class<?> type, String contentType) {
    return isSupportedContentType(contentType) || canWrite(type);
  }

  private boolean canWrite(Class<?> type) {
    return !isTextPlainType(type) && objectMapper.canSerialize(type);
  }

  @Override
  public void write(Object value, HttpServletResponse response, String contentType) throws IOException {
    var content = getContentType(contentType);

    log.trace("Writing JSON response with Content-Type: {}", content);
    response.setContentType(content);

    try {
      response.getWriter().write(objectMapper.writeValueAsString(value));
    } catch (JsonProcessingException e) {
      var message = "Failed to convert the return value to JSON and write the response. Verify the return value is POJO and can be serialized";
      log.error(message, e);
      throw new ObjectSerializingException(message, e);
    }
  }

  private String getContentType(String contentType) {
    return (contentType != null) ? contentType : APPLICATION_JSON.getValue();
  }

  @Override
  public boolean canRead(Class<?> type, String contentType) {
    return isSupportedContentType(contentType) || canRead(type);
  }

  private boolean canRead(Class<?> type) {
    return !isTextPlainType(type) && objectMapper.canDeserialize(objectMapper.constructType(type));
  }

  // TODO: 22.11.2023. not used for now. needs to be implemented further when it's used
  @Override
  public Object read(Class<?> type, HttpServletRequest request, String contentType) throws IOException {
    return objectMapper.readValue(request.getReader(), type);
  }

  @Override
  public boolean isSupportedContentType(String contentType) {
    return APPLICATION_JSON.getValue().equals(contentType);
  }

}

