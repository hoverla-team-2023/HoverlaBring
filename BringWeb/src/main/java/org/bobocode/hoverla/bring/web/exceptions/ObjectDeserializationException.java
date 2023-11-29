package org.bobocode.hoverla.bring.web.exceptions;

import org.bobocode.hoverla.bring.web.servlet.resolver.HandlerMethodArgumentResolver;

/**
 * Exception thrown to indicate an error during the deserialization of an object,
 * typically encountered in argument resolvers.
 *
 * @see HandlerMethodArgumentResolver
 * @see GlobalControllerAdvice
 */
public class ObjectDeserializationException extends RuntimeException {

  public ObjectDeserializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ObjectDeserializationException(String message) {
    super(message);
  }

}
