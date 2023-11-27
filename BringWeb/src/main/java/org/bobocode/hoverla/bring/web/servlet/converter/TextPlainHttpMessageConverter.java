package org.bobocode.hoverla.bring.web.servlet.converter;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.TEXT_PLAIN;
import static org.bobocode.hoverla.bring.web.util.TypeUtils.isTextPlainType;

/**
 * Message converter for plain text responses. It converts the response body into a plain text string and
 * sets the Content-Type header to "text/plain".
 */
@Slf4j
@RequiredArgsConstructor
public class TextPlainHttpMessageConverter implements HttpMessageConverter {

  // Need it to convert request body to primitive types
  private final ObjectMapper objectMapper;

  @Override
  public boolean canWrite(Class<?> type, String contentType) {
    return isSupportedContentType(contentType) || isTextPlainType(type);
  }

  @Override
  public void write(Object value, HttpServletResponse response, String contentType) throws IOException {
    var content = getContentType(contentType);

    log.trace("Writing plain text response with content type: {}", content);
    response.setContentType(content);
    response.getWriter().write(value.toString());
  }

  private String getContentType(String contentType) {
    return (contentType != null) ? contentType : TEXT_PLAIN.getValue();
  }

  @Override
  public boolean canRead(Class<?> type, String contentType) {
    return isSupportedContentType(contentType) || isTextPlainType(type);
  }

  @Override
  public Object read(Class<?> type, HttpServletRequest request, String contentType) throws IOException {
    return objectMapper.readValue(request.getInputStream(), type);
  }

  @Override
  public boolean isSupportedContentType(String type) {
    return TEXT_PLAIN.getValue().equals(type);
  }

}