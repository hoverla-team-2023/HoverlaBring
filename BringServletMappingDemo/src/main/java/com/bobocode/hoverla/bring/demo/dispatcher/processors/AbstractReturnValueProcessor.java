package com.bobocode.hoverla.bring.demo.dispatcher.processors;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.bobocode.hoverla.bring.demo.dispatcher.converters.HttpMessageConverter;

public abstract class AbstractReturnValueProcessor implements ReturnValueProcessor {

  private final List<HttpMessageConverter> converters;

  public AbstractReturnValueProcessor(List<HttpMessageConverter> converters) {
    this.converters = converters;
  }

  @Override
  public abstract boolean processReturnValue(Object returnValue, Method method, HttpServletRequest request, HttpServletResponse response) throws IOException;

  protected abstract String getContentType(Object returnValue);

  protected HttpMessageConverter findConverter(Class<?> type, String contentType) {
    for (HttpMessageConverter converter : converters) {
      if (converter.canWrite(type, contentType)) {
        return converter;
      }
    }
    return null; // No suitable converter found
  }

}
