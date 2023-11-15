package com.bobocode.hoverla.bring.demo.dispatcher.converters;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public class TextPlainHttpMessageConverter implements HttpMessageConverter {

  @Override
  public void write(Object value, HttpServletResponse response, String contentType) throws IOException {
    response.setHeader("Content-Type", getContentType(contentType));

    response.getWriter().write(value.toString());
  }

  @Override
  public boolean canWrite(Class<?> type, String contentType) {
    return String.class == type;
  }

  @Override
  public String getSupportedContentType() {
    return "text/plain";
  }

  private String getContentType(String contentType) {
    return (contentType != null) ? contentType : "text/plain";
  }

}