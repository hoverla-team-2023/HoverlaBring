package org.bobocode.hoverla.bring.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.bobocode.hoverla.bring.web.exceptions.GlobalControllerAdvice;
import org.bobocode.hoverla.bring.web.exceptions.NotFoundException;
import org.bobocode.hoverla.bring.web.exceptions.UnexpectedBringException;
import org.bobocode.hoverla.bring.web.exceptions.resolvers.AnnotationBasedHandlerExceptionResolver;
import org.bobocode.hoverla.bring.web.exceptions.resolvers.HandlerExceptionResolver;
import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;
import org.bobocode.hoverla.bring.web.servlet.converter.JsonHttpMessageConverter;
import org.bobocode.hoverla.bring.web.servlet.converter.TextPlainHttpMessageConverter;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;
import org.bobocode.hoverla.bring.web.servlet.mapping.AnnotationBasedHandlerMapping;
import org.bobocode.hoverla.bring.web.servlet.mapping.HandlerMapping;
import org.bobocode.hoverla.bring.web.servlet.processor.PojoReturnValueProcessor;
import org.bobocode.hoverla.bring.web.servlet.processor.ResponseEntityReturnValueProcessor;
import org.bobocode.hoverla.bring.web.servlet.processor.ReturnValueProcessor;
import org.bobocode.hoverla.bring.web.servlet.processor.TextPlainReturnValueProcessor;
import org.bobocode.hoverla.bring.web.servlet.resolver.HandlerMethodArgumentResolver;
import org.bobocode.hoverla.bring.web.servlet.resolver.PathVariableArgumentResolver;
import org.bobocode.hoverla.bring.web.servlet.resolver.QueryParamArgumentResolver;
import org.bobocode.hoverla.bring.web.servlet.resolver.RequestBodyMethodArgumentResolver;
import org.bobocode.hoverla.bring.web.servlet.resolver.RequestEntityMethodArgumentResolver;
import org.bobocode.hoverla.bring.web.servlet.resolver.ServletArgumentResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Basic {@link HttpServlet} implementation
 * DispatcherServlet handles incoming requests, processes them,
 * and dispatches them to appropriate handler methods.
 */
@Slf4j
@Setter
@RequiredArgsConstructor
public class DispatcherServlet extends HttpServlet {

  private final ServletContext servletContext;

  private List<ReturnValueProcessor> returnValueProcessors;
  private List<HandlerMethodArgumentResolver> argumentResolvers;
  private List<HandlerMapping> handlerMappings;
  private List<HandlerExceptionResolver> exceptionResolvers;

  private final Object[] controllers;

  private final Object[] controllerAdvices;

  /**
   * Initializes the servlet with the given configuration.
   *
   * @param config The servlet configuration.
   *
   * @throws ServletException If an exception occurs during initialization.
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);

    log.info("Initializing DispatcherServlet...");
    var objectMapper = new ObjectMapper();

    List<HttpMessageConverter> converters = List.of(new TextPlainHttpMessageConverter(objectMapper),
                                                    new JsonHttpMessageConverter(objectMapper),
                                                    new JsonHttpMessageConverter(new ObjectMapper()));

    this.returnValueProcessors = List.of(new PojoReturnValueProcessor(converters),
                                         new ResponseEntityReturnValueProcessor(converters),
                                         new TextPlainReturnValueProcessor(converters));

    this.argumentResolvers = List.of(new QueryParamArgumentResolver(),
                                     new ServletArgumentResolver(),
                                     new PathVariableArgumentResolver(),
                                     new RequestBodyMethodArgumentResolver(converters),
                                     new RequestEntityMethodArgumentResolver(converters));

    this.handlerMappings = List.of(new AnnotationBasedHandlerMapping(controllers)); // Need to provide Controllers what will be initialized and scanned
    this.exceptionResolvers = List.of(
      new AnnotationBasedHandlerExceptionResolver(ArrayUtils.addAll(new Object[] { new GlobalControllerAdvice() },
                                                                    controllerAdvices))); // Need to provide Controllers what will be initialized and scanned
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processRequest(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processRequest(req, resp);
  }

  @Override
  protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processRequest(req, resp);
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processRequest(req, resp);
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processRequest(req, resp);
  }

  @Override
  protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processRequest(req, resp);
  }

  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processRequest(req, resp);
  }

  /**
   * Processes the incoming request, delegates to the appropriate handler method, and handles the return value.
   *
   * @param request  The HTTP servlet request.
   * @param response The HTTP servlet response.
   *
   * @throws IOException If an I/O exception occurs.
   */
  private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      HandlerMethod handlerMethod = getHandlerMethod(request);

      if (handlerMethod != null) {

        Object[] resolvedArguments = resolveArguments(handlerMethod, request, response);

        Object returnValue = handlerMethod.handleRequest(resolvedArguments);

        processReturnValue(returnValue, handlerMethod, response);
      } else {
        throw new NotFoundException("Failed to find mapping for: %s method: %s".formatted(request.getRequestURI(), request.getMethod()));
      }
    } catch (Exception exception) {
      handleException(request, response, exception);
    }
  }

  private void handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
    log.debug("Handling exception:", exception);

    Exception exceptionToHandle = unwrapInvocationExceptionIfNeeded(exception);
    for (HandlerExceptionResolver resolver : exceptionResolvers) {
      if (resolver.canHandle(exceptionToHandle.getClass())) {
        try {
          HandlerMethod exceptionHandlerMethod = resolver.resolveException(exceptionToHandle);
          Object resolvedValue = exceptionHandlerMethod.handleRequest(exceptionToHandle);
          processReturnValue(resolvedValue, exceptionHandlerMethod, response);
          return;
        } catch (InvocationTargetException e) {
          log.error("Exception during processing exception", exception);
          throw new UnexpectedBringException("Exception during handling exception (%s)".formatted(exception.getMessage()), exception);
        }
      }
    }
    log.error("Unable to handle exception by exception resolvers", exception);
    throw new UnexpectedBringException("Unable to handle exception by exception resolvers (%s)".formatted(exception.getMessage()), exception);
  }

  private Exception unwrapInvocationExceptionIfNeeded(Exception exception) {
    Exception exceptionToHandle = exception;
    if (exception instanceof InvocationTargetException) {
      exceptionToHandle = (Exception) ((InvocationTargetException) exception).getTargetException();
    }
    return exceptionToHandle;
  }

  /**
   * Finds the appropriate handler method for the incoming request.
   *
   * @param request The HTTP servlet request.
   *
   * @return The handler method or {@code null} if not found.
   */
  private HandlerMethod getHandlerMethod(HttpServletRequest request) {
    for (HandlerMapping handlerMapping : handlerMappings) {
      HandlerMethod handlerMethod = handlerMapping.getHandlerMethod(request);
      if (handlerMethod != null) {
        return handlerMethod;
      }
    }
    return null;
  }

  /**
   * Resolves arguments for the handler method using registered argument resolvers.
   *
   * @param handlerMethod The handler method.
   * @param request       The HTTP servlet request.
   * @param response      The HTTP servlet response.
   *
   * @return An array of resolved arguments.
   */

  private Object[] resolveArguments(HandlerMethod handlerMethod, HttpServletRequest request, HttpServletResponse response) {
    return Arrays.stream(handlerMethod.getParameters())
      .flatMap(parameter -> argumentResolvers.stream()
        .filter(resolver -> resolver.supportsParameter(parameter))
        .map(resolver -> resolver.resolveArgument(handlerMethod, parameter, request, response)))
      .toList()
      .toArray();
  }

  /**
   * Processes the return value using registered return value processors.
   *
   * @param returnValue The return value from the handler method.
   * @param method      The handler method.
   * @param response    The HTTP servlet response.
   *
   * @throws IOException If an I/O exception occurs.
   */
  private void processReturnValue(Object returnValue, HandlerMethod method, HttpServletResponse response) throws IOException {
    //  todo  if (returnValue != null) ??
    for (ReturnValueProcessor processor : returnValueProcessors) {
      if (processor.supports(returnValue.getClass())) {
        if (processor.processReturnValue(returnValue, method, response)) {
          return;
        }
      }
    }

    sendInternalServerError(response);
  }

  private void sendInternalServerError(HttpServletResponse response) {
    try {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
    } catch (IOException ioException) {
      log.error("Exception during process request", ioException);
    }
  }

}
