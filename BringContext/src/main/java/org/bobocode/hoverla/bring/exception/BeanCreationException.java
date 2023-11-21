package org.bobocode.hoverla.bring.exception;

/**
 * {@code BeanCreationException} is a runtime exception thrown for errors during bean creation.
 */
public class BeanCreationException extends RuntimeException {

  /**
   * Constructs a new BeanCreationException with the specified detail message.
   *
   * @param message the detail message
   */
  public BeanCreationException(String message) {
    super(message);
  }

  /**
   * Constructs a new BeanCreationException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   */
  public BeanCreationException(String message, Throwable cause) {
    super(message, cause);
  }
}

