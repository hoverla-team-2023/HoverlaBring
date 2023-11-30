package org.bobocode.hoverla.bring.web.exceptions;

/**
 * Exception thrown when an issue occurs during the registration of an exception handler method.
 * This may happen if the method does not comply with the required signature or if there are
 * other issues with the registration process.
 */
public class RegisteringHandlerExceptionMethodException  extends RuntimeException{

  public RegisteringHandlerExceptionMethodException(String message) {
    super(message);
  }

}
