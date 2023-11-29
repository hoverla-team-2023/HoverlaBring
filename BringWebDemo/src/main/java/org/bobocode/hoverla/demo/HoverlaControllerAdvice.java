package org.bobocode.hoverla.demo;

import org.bobocode.hoverla.bring.web.annotations.ControllerAdvice;
import org.bobocode.hoverla.bring.web.annotations.ExceptionHandler;
import org.bobocode.hoverla.bring.web.annotations.StatusCode;

@ControllerAdvice
public class HoverlaControllerAdvice {

  @ExceptionHandler(HoverlaException.class)
  @StatusCode(value = 400)
  public BringError hoverla(HoverlaException exception) {
    return new BringError(exception.getMessage());
  }

  record BringError(String errorMessage) {}

}
