package org.bobocode.hoverla.bring.exception;

public class BeanInjectionException extends RuntimeException {

  public BeanInjectionException(String message) {
    super(message);
  }

  public BeanInjectionException(String message, Throwable cause) {
    super(message, cause);
  }

}
