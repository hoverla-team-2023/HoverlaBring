package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.lang.reflect.Parameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

/**
 * Interface to be implemented by classes that can resolve method parameters
 * in the context of a handler method.
 *
 * <p>A {@code HandlerMethodArgumentResolver} is typically used to inspect the
 * method parameters of a handler method and provide resolved values based
 * on the current request context.
 *
 * <p>Implementations are responsible for inspecting the method parameters and
 * providing the necessary values based on the current HTTP request context.
 * They play a crucial role in converting HTTP requests into method invocations
 * on handlers.
 */
public interface HandlerMethodArgumentResolver {

  /**
   * Determines if this resolver supports the given method parameter.
   *
   * @param parameter the method parameter to check
   *
   * @return {@code true} if this resolver can handle the parameter; {@code false} otherwise
   */
  boolean supportsParameter(Parameter parameter);

  /**
   * Resolves the value for the given method parameter.
   *
   * @param parameter the method parameter to resolve
   * @param request   the current HTTP request
   * @param response  the current HTTP response
   *
   * @return the resolved argument value for the method parameter
   */
  Object resolveArgument(HandlerMethod handlerMethod, Parameter parameter, HttpServletRequest request, HttpServletResponse response);

}

