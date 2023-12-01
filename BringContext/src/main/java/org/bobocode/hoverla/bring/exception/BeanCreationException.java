package org.bobocode.hoverla.bring.exception;

/**
 * {@code BeanCreationException} is a runtime exception that is thrown to indicate an error
 * during the creation of a bean in Hoverla Bring. It extends {@link RuntimeException} and
 * provides constructors for creating instances with a custom message and an optional cause.
 *
 * <p>This exception typically occurs when there is a failure in creating or initializing
 * a bean within the Hoverla Bring container.
 */
public class BeanCreationException extends RuntimeException {

  public BeanCreationException(String message) {
    super(message);
  }

  public BeanCreationException(String message, Throwable cause) {
    super(message, cause);
  }
}