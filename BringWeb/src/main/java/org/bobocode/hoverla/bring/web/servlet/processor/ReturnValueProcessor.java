package org.bobocode.hoverla.bring.web.servlet.processor;

import java.io.IOException;
import java.lang.reflect.Type;

import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.servlet.entity.ResponseEntity;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

/**
 * Converts the {@link org.bobocode.hoverla.bring.web.annotations.Controller controller} return value
 * and set it as a {@link org.bobocode.hoverla.bring.web.servlet.DispatcherServlet servlet} response.
 *
 * @see AbstractReturnValueProcessor
 * @see TextPlainReturnValueProcessor
 * @see ResponseEntityReturnValueProcessor
 * @see PojoReturnValueProcessor
 * @see ResponseEntity
 */
public interface ReturnValueProcessor {

  /**
   * Checks if the processing of the return type is supported.
   *
   * @param type the return type
   *
   * @return true if the return type is supported
   */
  boolean supports(Type type);

  /**
   * Converts the return value of the method and set it to the {@link HttpServletResponse response}.
   *
   * @param returnValue   result value of the endpoint
   * @param handlerMethod controller method
   * @param response      servlet response
   *
   * @return true if the return value was processed, false otherwise
   */
  boolean processReturnValue(Object returnValue, HandlerMethod handlerMethod, HttpServletResponse response) throws IOException;

}