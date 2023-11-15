package com.bobocode.hoverla.bring.demo.dispatcher.converters;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public interface HttpMessageConverter {

  void write(Object value, HttpServletResponse response, String contentType) throws IOException;

  boolean canWrite(Class<?> type, String contentType);

  String getSupportedContentType();

  //todo add canRead, canWrite

}