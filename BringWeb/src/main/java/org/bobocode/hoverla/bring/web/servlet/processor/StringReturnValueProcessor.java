package org.bobocode.hoverla.bring.web.servlet.processor;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

import lombok.extern.slf4j.Slf4j;

/**
 * The StringReturnValueProcessor class is an implementation of the ReturnValueProcessor interface.
 * It is responsible for handling method return values that are of type String or its subclasses in a servlet context.
 */
@Slf4j
public class StringReturnValueProcessor implements ReturnValueProcessor {

  /**
   * This method checks if the given return type is String or a subclass of String. It determines if this processor can handle the return value.
   */
  @Override
  public boolean supports(Class<?> type) {
    return String.class.isAssignableFrom(type);
  }

  /**
   * This method processes the return value from a controller method. It checks if the return value is a String, and if so,
   * writes it to the HttpServletResponse writer. This writes the String return value directly to the response body.
   */
  @Override
  public boolean processReturnValue(Object returnValue, HandlerMethod method, HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (returnValue instanceof String) {
      response.getWriter().write((String) returnValue);
      return true;
    } else {
      return false;
    }
  }

}

