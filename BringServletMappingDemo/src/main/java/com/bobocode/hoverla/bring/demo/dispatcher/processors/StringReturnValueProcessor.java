package com.bobocode.hoverla.bring.demo.dispatcher.processors;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.bobocode.hoverla.bring.demo.dispatcher.converters.HttpMessageConverter;

public class StringReturnValueProcessor extends AbstractReturnValueProcessor {

  public StringReturnValueProcessor(List<HttpMessageConverter> converters) {
    super(converters);
  }

  @Override
  public boolean supports(Class<?> type) {
    return type.isAssignableFrom(String.class);
  }

  @Override
  public boolean processReturnValue(Object returnValue, Method method, HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (returnValue instanceof String) {
      String responseBody = (String) returnValue;

      // Set the Content-Type header
      response.setContentType(getContentType(returnValue));

      // Write the response body to the output stream
      try (PrintWriter writer = response.getWriter()) {
        writer.write(responseBody);
      }

      return true; // Successfully processed the return value
    }

    return false; // Unable to process the return value
  }

  @Override
  protected String getContentType(Object returnValue) {
    return "text/plain";
  }

}