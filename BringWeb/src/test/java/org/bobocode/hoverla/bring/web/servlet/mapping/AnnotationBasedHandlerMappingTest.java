package org.bobocode.hoverla.bring.web.servlet.mapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AnnotationBasedHandlerMappingTest {

  @Mock
  private HttpServletRequest request;

  private Object[] controllers;

  @InjectMocks
  private AnnotationBasedHandlerMapping handlerMapping;

  @BeforeEach
  void setUp() throws ServletException {

    handlerMapping = new AnnotationBasedHandlerMapping(controllers);
  }

  @Test
  public void testGetHandlerMethodWithMatchingPathAndMethod() {
    // Arrange
    when(request.getRequestURI()).thenReturn("/test");
    when(request.getMethod()).thenReturn("GET");

    // Act
    HandlerMethod result = handlerMapping.getHandlerMethod(request);

    // Assert
    assertEquals(controllers[0].getClass().getName() + ".testMethod:GET", result.getPath());
  }

  @Test
  public void testGetHandlerMethodWithNoMatchingPath() {

    // Mock the request with a different path
    when(request.getRequestURI()).thenReturn("/other");

    // Act
    HandlerMethod result = handlerMapping.getHandlerMethod(request);

    // Assert
    assertNull(result);
  }

  @Test
  public void testGetHandlerMethodWithNoMatchingMethod() {

    // Mock the request with a different method
    when(request.getMethod()).thenReturn("POST");

    // Act
    HandlerMethod result = handlerMapping.getHandlerMethod(request);

    // Assert
    assertNull(result);
  }

}


