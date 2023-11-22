package org.bobocode.hoverla.bring.web.servlet.converter;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;

import static org.bobocode.hoverla.bring.web.servlet.converter.ContentType.TEXT_PLAIN;
import static org.bobocode.hoverla.bring.web.util.TypeUtils.isTextPlainType;

/**
 * Message converter for plain text responses. It converts the response body into a plain text string and
 * sets the Content-Type header to "text/plain".
 */
@Log4j2
public class TextPlainHttpMessageConverter implements HttpMessageConverter {

  @Override
  public boolean canWrite(Class<?> type, String contentType) {
    return isSupportedContentType(contentType) || isTextPlainType(type);
  }

  @Override
  public void write(Object value, HttpServletResponse response, String contentType) throws IOException {
    var content = getContentType(contentType);

    log.trace("Writing plain text response with content type: {}", content);
    response.setHeader(CONTENT_TYPE_HEADER, content);
    response.getWriter().write(value.toString());
  }

  private String getContentType(String contentType) {
    return (contentType != null) ? contentType : TEXT_PLAIN.getValue();
  }

  @Override
  public boolean canRead(Class<?> type, String contentType) {
    return isSupportedContentType(contentType) || isTextPlainType(type);
  }

  // TODO: 22.11.2023. not used for now. needs to be implemented further when it's used
  @Override
  public Object read(Class<?> type, HttpServletRequest request, String contentType) throws IOException {
    return null;
  }

  @Override
  public boolean isSupportedContentType(String type) {
    return TEXT_PLAIN.getValue().equals(type);
  }

}