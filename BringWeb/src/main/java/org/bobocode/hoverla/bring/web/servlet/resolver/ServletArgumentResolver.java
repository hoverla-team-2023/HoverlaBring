package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.lang.reflect.Parameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The ServletArgumentResolver class is a custom implementation of the HandlerMethodArgumentResolver interface in Spring MVC.
 * It resolves the HttpServletRequest and HttpServletResponse objects as method arguments in a controller method.
 */
public class ServletArgumentResolver implements HandlerMethodArgumentResolver {

  /**
   * Returns true if the given parameter is of type HttpServletRequest or HttpServletResponse, false otherwise.
   *
   * @param parameter the parameter to be checked
   *
   * @return true if the given parameter is of type HttpServletRequest or HttpServletResponse, false otherwise
   */
  @Override
  public boolean supportsParameter(Parameter parameter) {
    return HttpServletRequest.class.isAssignableFrom(parameter.getType()) ||
           HttpServletResponse.class.isAssignableFrom(parameter.getType());
  }

  /**
   * Resolves the HttpServletRequest or HttpServletResponse object as a method argument in a controller method.
   *
   * @param parameter the parameter to be resolved
   * @param request   the HttpServletRequest object
   * @param response  the HttpServletResponse object
   *
   * @return the resolved argument
   */
  @Override
  public Object resolveArgument(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
    if (HttpServletRequest.class.isAssignableFrom(parameter.getType())) {
      return request;
    } else if (HttpServletResponse.class.isAssignableFrom(parameter.getType())) {
      return response;
    } else {
      throw new IllegalArgumentException("Unsupported parameter type: " + parameter.getType());
    }
  }

}
