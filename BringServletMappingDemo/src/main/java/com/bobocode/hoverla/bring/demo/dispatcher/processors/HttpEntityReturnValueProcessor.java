package com.bobocode.hoverla.bring.demo.dispatcher.processors;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.bobocode.hoverla.bring.demo.dispatcher.HttpEntity;
import com.bobocode.hoverla.bring.demo.dispatcher.converters.HttpMessageConverter;

public class HttpEntityReturnValueProcessor extends AbstractReturnValueProcessor {

  public HttpEntityReturnValueProcessor(List<HttpMessageConverter> converters) {
    super(converters);
  }

  @Override
  public boolean supports(Class<?> type) {
    return HttpEntity.class.isAssignableFrom(type);
  }

  @Override
  protected String getContentType(Object returnValue) {
    if (returnValue instanceof HttpEntity) {
      HttpEntity<?> httpEntity = (HttpEntity<?>) returnValue;
      Map<String, List<String>> headers = httpEntity.getHeaders();
      if (headers != null) {
        List<String> contentType = headers.get("Content-Type");
        if (contentType != null) {
          return contentType
            .stream()
            .findFirst()
            .orElse(null);
        }
      }
    }
    return null;
  }

  @Override
  public boolean processReturnValue(Object returnValue, Method method, HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (returnValue instanceof HttpEntity) {
      HttpEntity<?> httpEntity = (HttpEntity<?>) returnValue;
      Object body = httpEntity.getBody();
      Map<String, List<String>> headers = httpEntity.getHeaders();

      // Find the appropriate converter for the body type and MIME type
      HttpMessageConverter converter = findConverter(body.getClass(), getContentType(returnValue));

      if (converter != null) {
        // Set headers from the HttpEntity
        setHeaders(headers, response);

        // Write the response using the converter
        converter.write(body, response, getContentType(returnValue));
        return true; // Successfully processed the return value
      }
    }

    return false; // Unable to process the return value
  }

  private void setHeaders(Map<String, List<String>> headers, HttpServletResponse response) {
    for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
      String name = entry.getKey();
      List<String> values = entry.getValue();
      for (String value : values) {
        response.addHeader(name, value);
      }
    }
  }

}
