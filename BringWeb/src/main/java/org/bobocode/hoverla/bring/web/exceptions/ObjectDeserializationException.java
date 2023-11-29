package org.bobocode.hoverla.bring.web.exceptions;

public class ObjectDeserializationException extends RuntimeException {

  public ObjectDeserializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ObjectDeserializationException(String message) {
    super(message);
  }

}
