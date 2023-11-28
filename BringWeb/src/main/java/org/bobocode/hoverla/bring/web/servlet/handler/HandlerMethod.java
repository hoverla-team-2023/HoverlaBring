package org.bobocode.hoverla.bring.web.servlet.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.bobocode.hoverla.bring.web.annotations.RequestMethod;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a handler method that can process incoming requests.
 */
@Data
@Slf4j
@RequiredArgsConstructor
public class HandlerMethod {

  /**
   * The type of the bean containing the handler method.
   */
  private final Class<?> beanType;

  /**
   * The handler method.
   */
  private final Method method;

  /**
   * The path associated with the handler method.
   */
  private final String path;

  /**
   * The parameters of the handler method.
   */
  private final Parameter[] parameters;

  /**
   * The instance of the bean containing the handler method.
   */
  private final Object bean;

  /**
   * The enum of the  HTTP  request methods.
   */
  private final RequestMethod requestMethod;

  /**
   * Handles the incoming request by invoking the handler method with resolved arguments.
   *
   * @param resolvedArguments The arguments resolved for the handler method.
   *
   * @return The result of invoking the handler method.
   *
   * @throws RuntimeException If an exception occurs during method invocation.
   */
  public Object handleRequest(Object... resolvedArguments) {
    try {
      return method.invoke(bean, resolvedArguments);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

}
