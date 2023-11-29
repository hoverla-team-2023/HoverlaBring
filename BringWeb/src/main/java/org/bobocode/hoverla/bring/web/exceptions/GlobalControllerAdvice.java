package org.bobocode.hoverla.bring.web.exceptions;

import org.bobocode.hoverla.bring.web.annotations.ControllerAdvice;
import org.bobocode.hoverla.bring.web.annotations.ExceptionHandler;
import org.bobocode.hoverla.bring.web.annotations.StatusCode;

@ControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(ObjectDeserializationException.class)
  @StatusCode(value = 400)
  public BringError handle(ObjectDeserializationException exception) {
    return new BringError(exception.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  @StatusCode(value = 404)
  public BringError handle(NotFoundException exception) {
    return new BringError(exception.getMessage());
  }

  record BringError(String errorMessage) {}

}


