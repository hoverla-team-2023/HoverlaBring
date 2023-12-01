package org.bobocode.hoverla.bring.web.exceptions;

import org.bobocode.hoverla.bring.web.annotations.ExceptionHandler;
import org.bobocode.hoverla.bring.web.annotations.StatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalControllerAdviceTest {

  private GlobalControllerAdvice instance;

  @BeforeEach
  void setup() {
    instance = new GlobalControllerAdvice();
  }

  @Test
  void givenObjectDeserializationException_whenHandle_thenReturnBringErrorWith400StatusCode() throws NoSuchMethodException {
    var message = "Object deserialization exception";
    var exception = new ObjectDeserializationException(message);

    var result = instance.handle(exception);

    assertEquals(message, result.errorMessage());

    verifyAnnotations(ObjectDeserializationException.class, 400);
  }

  @Test
  void givenInvalidPathVariableException_whenHandle_thenReturnBringErrorWith400StatusCode() throws NoSuchMethodException {
    var message = "Object deserialization exception";
    var exception = new InvalidPathVariableException(message, new RuntimeException());

    var result = instance.handle(exception);

    assertEquals(message, result.errorMessage());

    verifyAnnotations(InvalidPathVariableException.class, 400);
  }

  @Test
  void givenNotFoundException_whenHandle_thenReturnBringErrorWith404StatusCode() throws NoSuchMethodException {
    var message = "Object deserialization exception";
    var exception = new NotFoundException(message);

    var result = instance.handle(exception);

    assertEquals(message, result.errorMessage());

    verifyAnnotations(NotFoundException.class, 404);
  }

  private void verifyAnnotations(Class<?> exceptionClass, int expectedStatusCodeValue) throws NoSuchMethodException {
    var method = instance.getClass().getDeclaredMethod("handle", exceptionClass);

    var exceptionHandlerAnnotation = method.getAnnotation(ExceptionHandler.class);
    assertEquals(exceptionClass, exceptionHandlerAnnotation.value());

    var statusCodeAnnotation = method.getAnnotation(StatusCode.class);
    assertEquals(expectedStatusCodeValue, statusCodeAnnotation.value());
  }

}