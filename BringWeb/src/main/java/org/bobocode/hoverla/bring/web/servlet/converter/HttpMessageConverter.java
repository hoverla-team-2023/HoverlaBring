package org.bobocode.hoverla.bring.web.servlet.converter;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This interface is used to convert and write the return value of a {@link org.bobocode.hoverla.bring.web.annotations.Controller} to a {@link HttpServletResponse}.
 */
public interface HttpMessageConverter {

  String CONTENT_TYPE_HEADER = "Content-Type";

  boolean canWrite(Class<?> type, String contentType);

  void write(Object value, HttpServletResponse response, String contentType) throws IOException;

  boolean canRead(Class<?> type, String contentType);

  Object read(Class<?> type, HttpServletRequest request, String contentType) throws IOException;

  boolean isSupportedContentType(String contentType);

}
