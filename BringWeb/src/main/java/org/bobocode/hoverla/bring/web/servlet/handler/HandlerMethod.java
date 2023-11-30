package org.bobocode.hoverla.bring.web.servlet.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import org.bobocode.hoverla.bring.web.annotations.RequestMethod;
import org.bobocode.hoverla.bring.web.exceptions.InvocationHandleMethodException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a handler method that can process incoming requests or handle exceptions.
 */
@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandlerMethod {

  /**
   * The type of the bean containing the handler method.
   */
  private Class<?> beanType;

  /**
   * The handler method.
   */
  private Method method;

  /**
   * The path associated with the handler method. Optional
   */
  private String path;

  /**
   * The parameters of the handler method.
   */
  private Parameter[] parameters;

  /**
   * The instance of the bean containing the handler method.
   */
  private Object bean;

  /**
   * The enum of the  HTTP  request methods. Optional
   */
  private RequestMethod requestMethod;

  private Type genericReturnType;

  /**
   * Handles the incoming request by invoking the handler method with resolved arguments.
   *
   * @param resolvedArguments The arguments resolved for the handler method.
   *
   * @return The result of invoking the handler method.
   *
   * @throws InvocationHandleMethodException If an exception occurs during the method invocation.
   */
  public Object handleRequest(Object... resolvedArguments) throws InvocationTargetException {
    try {
      return method.invoke(bean, resolvedArguments);
    } catch (IllegalAccessException e) {
      throw new InvocationHandleMethodException("Failed to invoke handler method", e);
    }
  }

}
