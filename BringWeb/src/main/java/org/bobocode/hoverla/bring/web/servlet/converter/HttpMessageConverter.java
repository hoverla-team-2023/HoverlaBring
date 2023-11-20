package org.bobocode.hoverla.bring.web.servlet.converter;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HttpMessageConverter {

  boolean canWrite(Class<?> type, String contentType);

  void write(Object value, HttpServletResponse response, String contentType) throws IOException;

  boolean canRead(Class<?> type, String contentType);

  void read(Object value, HttpServletRequest request, String contentType) throws IOException;

  String getSupportedContentType();

}
