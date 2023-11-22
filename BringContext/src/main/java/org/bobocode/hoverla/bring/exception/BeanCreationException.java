package org.bobocode.hoverla.bring.exception;

public class BeanCreationException extends RuntimeException {

  public BeanCreationException(String message) {
    super(message);
  }

  public BeanCreationException(String message, Throwable cause) {
    super(message, cause);
  }

}
