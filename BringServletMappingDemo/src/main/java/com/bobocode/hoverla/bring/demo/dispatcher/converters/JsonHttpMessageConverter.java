package com.bobocode.hoverla.bring.demo.dispatcher.converters;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHttpMessageConverter implements HttpMessageConverter {

  public static final String SUPPORTED_CONTENT_TYPE = "application/json";
  private final ObjectMapper objectMapper;

  public JsonHttpMessageConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void write(Object value, HttpServletResponse response, String contentType) throws IOException {
    response.setHeader("Content-Type", getContentType(contentType));
    response.getWriter().write(objectMapper.writeValueAsString(value));
  }

  @Override
  public boolean canWrite(Class<?> type, String contentType) {
    return contentType == null || contentType.equals(getSupportedContentType());
  }

  @Override
  public String getSupportedContentType() {
    return SUPPORTED_CONTENT_TYPE;
  }

  private String getContentType(String contentType) {
    return (contentType != null) ? contentType : SUPPORTED_CONTENT_TYPE;
  }

}

