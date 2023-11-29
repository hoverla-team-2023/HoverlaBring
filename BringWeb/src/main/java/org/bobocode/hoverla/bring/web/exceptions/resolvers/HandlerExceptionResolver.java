package org.bobocode.hoverla.bring.web.exceptions.resolvers;

import org.bobocode.hoverla.bring.web.exceptions.InvocationHandleMethodException;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

/**
 * Represents a method annotated with {@code @ExceptionHandler} in a {@code @ControllerAdvice} class.
 * It encapsulates the necessary information to invoke the method and handle exceptions during invocation.
 */
public interface HandlerExceptionResolver {

  boolean canHandle(Class<? extends Exception> clazz);

  /**
   * Resolve the given exception that was thrown during request processing.
   *
   * @param exception the exception to be resolved
   *
   * @return an object representing the result of the resolution
   *
   * @throws InvocationHandleMethodException if an error occurs during the resolution process
   */
  HandlerMethod resolveException(Exception exception);

}
