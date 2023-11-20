package org.bobocode.hoverla.bring.web.servlet.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Represents a handler method that can process incoming requests.
 */
@Data
@Log4j2
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
