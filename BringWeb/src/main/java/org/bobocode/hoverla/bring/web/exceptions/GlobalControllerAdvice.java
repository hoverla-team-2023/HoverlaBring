package org.bobocode.hoverla.bring.web.exceptions;

import org.bobocode.hoverla.bring.web.annotations.ControllerAdvice;
import org.bobocode.hoverla.bring.web.annotations.ExceptionHandler;
import org.bobocode.hoverla.bring.web.annotations.StatusCode;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handling advice for the entire application.
 * Annotated with {@link ControllerAdvice}, this class provides methods to handle specific exceptions
 * thrown during request processing. The handled exceptions are typically presented with customized
 * error responses, including specific HTTP status codes and error messages.
 *
 * <p>The methods in this class are annotated with {@link ExceptionHandler}, specifying the types of
 * exceptions they can handle.
 *
 * @see ControllerAdvice
 * @see ExceptionHandler
 * @see StatusCode
 */

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(ObjectDeserializationException.class)
  @StatusCode(value = 400)
  public BringError handle(ObjectDeserializationException exception) {
    log.debug("Handling ObjectDeserializationException by global controller advice", exception);
    return new BringError(exception.getMessage());
  }

  @ExceptionHandler(InvalidPathVariableException.class)
  @StatusCode(value = 400)
  public BringError handle(InvalidPathVariableException exception) {
    log.debug("Handling InvalidPathVariableException by global controller advice", exception);
    return new BringError(exception.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  @StatusCode(value = 404)
  public BringError handle(NotFoundException exception) {
    log.debug("Handling NotFoundException by global controller advice", exception);
    return new BringError(exception.getMessage());
  }

  record BringError(String errorMessage) {}

}


