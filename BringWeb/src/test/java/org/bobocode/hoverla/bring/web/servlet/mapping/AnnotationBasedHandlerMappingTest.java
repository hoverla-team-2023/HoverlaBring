package org.bobocode.hoverla.bring.web.servlet.mapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import org.bobocode.hoverla.bring.web.annotations.Controller;
import org.bobocode.hoverla.bring.web.annotations.ExceptionHandler;
import org.bobocode.hoverla.bring.web.annotations.PathVariable;
import org.bobocode.hoverla.bring.web.annotations.RequestMapping;
import org.bobocode.hoverla.bring.web.annotations.RequestMethod;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnnotationBasedHandlerMappingTest {

  @Mock
  private HttpServletRequest request;

  private final Object[] controllers = new Object[] { new MockHandler(), new InvalidMockHandler1(), new InvalidMockHandler2() };

  private AnnotationBasedHandlerMapping handlerMapping;

  @BeforeEach
  void setUp() throws ServletException {

    handlerMapping = new AnnotationBasedHandlerMapping(controllers);
  }

  @Test
  public void testGetHandlerMethodWithMatchingPathAndMethod() {
    // Arrange
    when(request.getRequestURI()).thenReturn("/conversations/123/notes/321");
    when(request.getMethod()).thenReturn("GET");

    // Act
    HandlerMethod result = handlerMapping.getHandlerMethod(request);

    // Assert
    assertEquals("/conversations/{conversationId}/notes/{noteId}", result.getPath());
  }

  @Test
  public void testGetHandlerMethodWithNoMatchingPath() {

    // Mock the request with a different path
    when(request.getRequestURI()).thenReturn("/other");
    when(request.getMethod()).thenReturn("GET");

    // Act
    HandlerMethod result = handlerMapping.getHandlerMethod(request);

    // Assert
    assertNull(result);
  }

  @Test
  public void testGetHandlerMethodWithNoMatchingMethod() {

    // Mock the request with a different method
    when(request.getRequestURI()).thenReturn("/other");
    when(request.getMethod()).thenReturn("POST");

    // Act
    HandlerMethod result = handlerMapping.getHandlerMethod(request);

    // Assert
    assertNull(result);
  }

  static class CustomException extends RuntimeException {}

  static class SubCustomException extends CustomException {}

  static class SubSubCustomException extends SubCustomException {}

  // Mock controller advice class
  @Controller
  static class MockHandler {

    @ExceptionHandler(CustomException.class)
    public void handleCustomException(CustomException exception) {
      // Mock handler method
    }

    @ExceptionHandler(SubCustomException.class)
    public void handleSubCustomException(SubCustomException subCustomException) {
      // Mock handler method
    }

    @RequestMapping(path = "/conversations/{conversationId}/notes/{noteId}", method = RequestMethod.GET)
    public void exampleMethod(@PathVariable("conversationId") String conversationId, @PathVariable("noteId") Long noteId) {

    }

  }

  @Controller
  static class InvalidMockHandler1 {

    @ExceptionHandler(Exception.class)
    public void handleCustomException(CustomException exception) {
      // Mock handler method
    }

  }

  @Controller
  static class InvalidMockHandler2 {

    @ExceptionHandler(Exception.class)
    public void handleCustomException() {
      // Mock handler method
    }

  }

}


