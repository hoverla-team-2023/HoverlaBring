package org.bobocode.hoverla.bring.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.exceptions.NotFoundException;
import org.bobocode.hoverla.bring.web.exceptions.ObjectDeserializationException;
import org.bobocode.hoverla.bring.web.exceptions.resolvers.HandlerExceptionResolver;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;
import org.bobocode.hoverla.bring.web.servlet.mapping.HandlerMapping;
import org.bobocode.hoverla.bring.web.servlet.processor.ReturnValueProcessor;
import org.bobocode.hoverla.bring.web.servlet.resolver.HandlerMethodArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DispatcherServletTest {

  private ServletContext servletContext;

  @Mock
  private ReturnValueProcessor returnValueProcessor1;

  @Mock
  private ReturnValueProcessor returnValueProcessor2;

  @Mock
  private HandlerMethodArgumentResolver argumentResolver1;

  @Mock
  private HandlerMethodArgumentResolver argumentResolver2;

  @Mock
  private HandlerMapping handlerMapping1;

  @Mock
  private HandlerMapping handlerMapping2;

  @Mock
  private HandlerExceptionResolver exceptionResolver1;

  @Mock
  private HandlerExceptionResolver exceptionResolver2;

  private Object[] controllers;

  private Object[] controllerAdvices;

  private DispatcherServlet dispatcherServlet;

  @BeforeEach
  void setUp() throws ServletException {

    dispatcherServlet = new DispatcherServlet(servletContext, controllers, controllerAdvices);

    // Set mocks using List.of
    dispatcherServlet.setReturnValueProcessors(List.of(returnValueProcessor1, returnValueProcessor2));
    dispatcherServlet.setArgumentResolvers(List.of(argumentResolver1, argumentResolver2));
    dispatcherServlet.setHandlerMappings(List.of(handlerMapping1, handlerMapping2));
    dispatcherServlet.setExceptionResolvers(List.of(exceptionResolver1, exceptionResolver2));
  }

  @Test
  public void testDoGetHappyPath() throws ServletException, IOException, InvocationTargetException {
    // Mock HttpServletRequest and HttpServletResponse
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    // Mock HandlerMethod for the GET request
    HandlerMethod handlerMethod = mock(HandlerMethod.class);
    when(handlerMapping1.getHandlerMethod(request)).thenReturn(handlerMethod);
    Parameter parameter1 = mock(Parameter.class);
    Parameter parameter2 = mock(Parameter.class);
    when(handlerMethod.getParameters()).thenReturn(new Parameter[] { parameter1, parameter2 });
    when(handlerMethod.getGenericReturnType()).thenReturn(String.class);

    when(argumentResolver1.supportsParameter(parameter1)).thenReturn(true);
    when(argumentResolver1.supportsParameter(parameter2)).thenReturn(true);

    when(argumentResolver2.supportsParameter(parameter1)).thenReturn(false);
    when(argumentResolver2.supportsParameter(parameter2)).thenReturn(false);

    // Mock resolved arguments
    Object resolvedArgument1 = "argument1";
    Object resolvedArgument2 = "argument2";

    when(argumentResolver1.resolveArgument(eq(handlerMethod), eq(parameter1), eq(request), eq(response)))
      .thenReturn(resolvedArgument1);

    when(argumentResolver1.resolveArgument(eq(handlerMethod), eq(parameter2), eq(request), eq(response)))
      .thenReturn(resolvedArgument2);

    // Mock return value from the handler method
    Object returnValue = "Hello, World!";
    doReturn(returnValue).when(handlerMethod).handleRequest(resolvedArgument1, resolvedArgument2);

    when(returnValueProcessor1.supports(returnValue.getClass())).thenReturn(false);
    when(returnValueProcessor2.supports(returnValue.getClass())).thenReturn(true);

    // Perform the GET request
    dispatcherServlet.doGet(request, response);

    // Verify that the response was processed correctly
    verify(returnValueProcessor2).processReturnValue(eq(returnValue), eq(handlerMethod), eq(response));
  }

  @Test
  public void testPostWhenNoMappingHandlerFound() throws ServletException, IOException, InvocationTargetException {
    // Mock HttpServletRequest and HttpServletResponse
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    doReturn(null).when(handlerMapping1).getHandlerMethod(request);

    doReturn(true).when(exceptionResolver1).canHandle(NotFoundException.class);
    HandlerMethod exceptionHandlerMethod = mock(HandlerMethod.class);
    doReturn(exceptionHandlerMethod).when(exceptionResolver1).resolveException(any(NotFoundException.class));
    String errorMessage = "Not found error message";
    doReturn(errorMessage).when(exceptionHandlerMethod).handleRequest(any(NotFoundException.class));
    doReturn(errorMessage.getClass()).when(exceptionHandlerMethod).getGenericReturnType();

    doReturn(true).when(returnValueProcessor1).supports(errorMessage.getClass());
    doReturn(true).when(returnValueProcessor1).processReturnValue(eq(errorMessage), eq(exceptionHandlerMethod), eq(response));

    dispatcherServlet.doPost(request, response);

    verify(returnValueProcessor1).processReturnValue(eq(errorMessage), eq(exceptionHandlerMethod), eq(response));
  }

  @Test
  public void testDoGetWhenArgumentResolvingFailed() throws ServletException, IOException, InvocationTargetException {
    // Mock HttpServletRequest and HttpServletResponse
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    // Mock HandlerMethod for the GET request
    HandlerMethod handlerMethod = mock(HandlerMethod.class);
    when(handlerMapping1.getHandlerMethod(request)).thenReturn(handlerMethod);
    Parameter parameter1 = mock(Parameter.class);
    Parameter parameter2 = mock(Parameter.class);
    when(handlerMethod.getParameters()).thenReturn(new Parameter[] { parameter1, parameter2 });

    when(argumentResolver1.supportsParameter(parameter1)).thenReturn(true);
    when(argumentResolver1.resolveArgument(eq(handlerMethod), eq(parameter1), eq(request), eq(response)))
      .thenThrow(new ObjectDeserializationException("Failed to deserialize query param"));

    when(exceptionResolver1.canHandle(ObjectDeserializationException.class)).thenReturn(true);
    HandlerMethod exceptionHandlerMethod = mock(HandlerMethod.class);
    doReturn(exceptionHandlerMethod).when(exceptionResolver1).resolveException(any(ObjectDeserializationException.class));
    String errorMessage = "error message";
    doReturn(errorMessage).when(exceptionHandlerMethod).handleRequest(any(ObjectDeserializationException.class));
    doReturn(String.class).when(exceptionHandlerMethod).getGenericReturnType();

    doReturn(true).when(returnValueProcessor1).supports(errorMessage.getClass());

    // Perform the GET request
    dispatcherServlet.doGet(request, response);

    verify(handlerMethod, never()).handleRequest(any());
    verify(returnValueProcessor1).processReturnValue(errorMessage, exceptionHandlerMethod, response);
  }

}