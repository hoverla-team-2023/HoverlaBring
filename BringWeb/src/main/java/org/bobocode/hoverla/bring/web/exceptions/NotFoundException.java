package org.bobocode.hoverla.bring.web.exceptions;

import org.bobocode.hoverla.bring.web.servlet.DispatcherServlet;

/**
 * Exception thrown when no handler method is found to process an incoming request.
 * This exception is typically thrown by the {@link DispatcherServlet}
 * when it cannot find a suitable handler method for the provided request.
 *
 * @see DispatcherServlet
 */
public class NotFoundException extends RuntimeException {

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotFoundException(String message) {
    super(message);
  }

}
