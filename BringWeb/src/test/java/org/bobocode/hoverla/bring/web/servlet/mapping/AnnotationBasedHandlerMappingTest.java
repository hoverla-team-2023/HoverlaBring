package org.bobocode.hoverla.bring.web.servlet.mapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import jakarta.servlet.http.HttpServletRequest;

import org.bobocode.hoverla.bring.web.annotations.RequestMapping;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class AnnotationBasedHandlerMappingTest {

  @InjectMocks
  private AnnotationBasedHandlerMapping handlerMapping;

  @Mock
  private HttpServletRequest request;

  private Object[] controllers;

  @BeforeEach
  public void setUp() {
    // Create a mock controller
    controllers = mock(String.valueOf(Object.class));
    // Create a mock method
    Method mockMethod = mock(Method.class);
    when(mockMethod.isAnnotationPresent(RequestMapping.class)).thenReturn(true);
    when(mockMethod.getAnnotation(RequestMapping.class)).thenReturn(mock(RequestMapping.class));
    when(mockMethod.getParameters()).thenReturn(new Parameter[]{});

    // Add the mock method to the mock controller
    when(controllers.getClass().getMethods()).thenReturn(new Method[]{mockMethod});

    // Create the handlerMapping with the mock controller
    handlerMapping = new AnnotationBasedHandlerMapping(controllers);

    when(request.getRequestURI()).thenReturn("/test");
    when(request.getMethod()).thenReturn("GET");
  }


  @Test
  public void testGetHandlerMethodWithMatchingPathAndMethod() {

    // Act
    HandlerMethod result = handlerMapping.getHandlerMethod(request);

    // Assert
    assertEquals(controllers.getClass().getName() + ".testMethod:GET", result.getPath());
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


