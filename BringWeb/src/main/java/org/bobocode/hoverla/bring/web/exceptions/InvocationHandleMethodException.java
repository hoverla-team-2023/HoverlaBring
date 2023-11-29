package org.bobocode.hoverla.bring.web.exceptions;

import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

/**
 * Exception thrown to indicate an error during the invocation of a handler method.
 * This exception wraps any checked exception thrown during the invocation of the handler method
 * and is typically used to propagate the error with additional context.
 *
 * <p>This exception is commonly thrown by the {@link HandlerMethod#handleRequest(Object...)} method
 * when it encounters an issue while invoking the underlying handler method.
 */
public class InvocationHandleMethodException extends RuntimeException {

  public InvocationHandleMethodException(String message, Throwable cause) {
    super(message, cause);
  }

}
