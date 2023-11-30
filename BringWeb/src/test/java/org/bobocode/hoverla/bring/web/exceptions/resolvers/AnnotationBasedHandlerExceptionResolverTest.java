package org.bobocode.hoverla.bring.web.exceptions.resolvers;

import org.bobocode.hoverla.bring.web.exceptions.RegisteringHandlerExceptionMethodException;
import org.bobocode.hoverla.bring.web.annotations.ControllerAdvice;
import org.bobocode.hoverla.bring.web.annotations.ExceptionHandler;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationBasedHandlerExceptionResolverTest {

  @Test
  void givenNoParameters_whenRegisterHandler_thenThrownException() {
    assertThrows(RegisteringHandlerExceptionMethodException.class,
                 () -> new AnnotationBasedHandlerExceptionResolver(new Object[] { new InvalidMockHandler1() }));
  }

  @Test
  void givenIncompatibleParameterType_whenRegisterHandler_thenThrownException() {
    assertThrows(RegisteringHandlerExceptionMethodException.class,
                 () -> new AnnotationBasedHandlerExceptionResolver(new Object[] { new InvalidMockHandler2() }));
  }

  @Test
  void whenCanHandle_thenShouldReturnTrueForMatchingException() {
    // Given
    AnnotationBasedHandlerExceptionResolver resolver = createResolverWithExceptionHandlers();
    Class<? extends Exception> exceptionType = CustomException.class;

    // When
    boolean result = resolver.canHandle(exceptionType);

    // Then
    assertTrue(result);
  }

  @Test
  void whenResolveException_thenShouldReturnHandlerForExactMatch() {
    // Given
    AnnotationBasedHandlerExceptionResolver resolver = createResolverWithExceptionHandlers();
    CustomException exception = new CustomException();

    // When
    HandlerMethod result = resolver.resolveException(exception);

    // Then
    assertEquals(MockHandler.class, result.getBeanType());
    assertEquals("handleCustomException", result.getMethod().getName());
  }

  @Test
  void whenResolveException_thenShouldReturnHandlerForClosestMatch() {
    // Given
    AnnotationBasedHandlerExceptionResolver resolver = createResolverWithExceptionHandlers();
    SubSubCustomException exception = new SubSubCustomException();

    // When
    HandlerMethod result = resolver.resolveException(exception);

    System.out.println(resolver.calculateHierarchyDepth(AnnotationBasedHandlerExceptionResolverTest.SubCustomException.class,
                                                        AnnotationBasedHandlerExceptionResolverTest.SubSubCustomException.class));

    assertEquals(MockHandler.class, result.getBeanType());
    assertEquals("handleSubCustomException", result.getMethod().getName());
  }

  private AnnotationBasedHandlerExceptionResolver createResolverWithExceptionHandlers() {
    Object[] controllerAdvices = { new MockHandler() };

    return new AnnotationBasedHandlerExceptionResolver(controllerAdvices);
  }

  // Custom exception classes for testing
  static class CustomException extends RuntimeException {
  }

  static class SubCustomException extends CustomException {
  }

  static class SubSubCustomException extends SubCustomException {
  }

  // Mock controller advice class
  @ControllerAdvice
  static class MockHandler {

    @ExceptionHandler(CustomException.class)
    public void handleCustomException(CustomException exception) {
      // Mock handler method
    }

    @ExceptionHandler(SubCustomException.class)
    public void handleSubCustomException(SubCustomException subCustomException) {
      // Mock handler method
    }

  }

  @ControllerAdvice
  static class InvalidMockHandler1 {

    @ExceptionHandler(Exception.class)
    public void handleCustomException(CustomException exception) {
      // Mock handler method
    }

  }

  @ControllerAdvice
  static class InvalidMockHandler2 {

    @ExceptionHandler(Exception.class)
    public void handleCustomException() {
      // Mock handler method
    }

  }

}