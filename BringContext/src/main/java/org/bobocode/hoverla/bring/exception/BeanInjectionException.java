package org.bobocode.hoverla.bring.exception;

/**
 * The BeanInjectionException is used to detect problems with bean injection.
 * Reasons for this exception include:
 * - No bean definition found (can happen with incorrect scanning paths or missing annotations).
 * - More than one bean to inject found (can occur when multiple beans implement the same interface and are marked with @Component).
 *
 * <p>This exception is thrown to indicate issues with the injection of beans during the application context initialization.
 *
 * @see RuntimeException
 */
public class BeanInjectionException extends RuntimeException {

  /**
   * Constructs a new BeanInjectionException with the specified detail message.
   *
   * @param message the detail message
   */
  public BeanInjectionException(String message) {
    super(message);
  }

  /**
   * Constructs a new BeanInjectionException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   */
  public BeanInjectionException(String message, Throwable cause) {
    super(message, cause);
  }

}

