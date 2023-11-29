package org.bobocode.hoverla.bring.web.exceptions;

/**
 * Exception thrown when an unsupported content type is encountered.
 */
public class UnsupportedContentTypeException extends IllegalArgumentException {

  public UnsupportedContentTypeException(String message) {
    super(message);
  }

}
