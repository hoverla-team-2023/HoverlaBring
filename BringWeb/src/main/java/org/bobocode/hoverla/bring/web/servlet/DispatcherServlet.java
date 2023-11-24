package org.bobocode.hoverla.bring.web.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
import org.bobocode.hoverla.bring.web.servlet.resolver.QueryParamArgumentResolver;
import org.bobocode.hoverla.bring.web.servlet.resolver.ServletArgumentResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Basic {@link HttpServlet} implementation
 * DispatcherServlet handles incoming requests, processes them,
 * and dispatches them to appropriate handler methods.
 */
@Log4j2
@RequiredArgsConstructor
public class DispatcherServlet extends HttpServlet {

  private final ServletContext servletContext;

  private List<ReturnValueProcessor> returnValueProcessors;
  private List<HandlerMethodArgumentResolver> argumentResolvers;
  private List<HandlerMapping> handlerMappings;

  private final Object[] controllers;

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
    // todo add logs
    List<HttpMessageConverter> converters = List.of(new TextPlainHttpMessageConverter(),
                                                    new JsonHttpMessageConverter(new ObjectMapper()));

    this.returnValueProcessors = List.of(new PojoReturnValueProcessor(converters),
                                         new ResponseEntityReturnValueProcessor(converters),
                                         new TextPlainReturnValueProcessor(converters));

    this.argumentResolvers = List.of(new QueryParamArgumentResolver(),
                                     new ServletArgumentResolver());
    this.handlerMappings = List.of(new AnnotationBasedHandlerMapping(controllers)); // Need to provide Controllers what will be initialized and scanned
  }
  //todo add doPost...
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

    HandlerMethod handlerMethod = getHandlerMethod(request);

    if (handlerMethod != null) {

      // Resolve arguments using registered argument resolvers
      Object[] resolvedArguments = resolveArguments(handlerMethod, request, response);

      // Invoke the handler method
      Object returnValue = handlerMethod.handleRequest(resolvedArguments);

      // Process the return value
      processReturnValue(returnValue, handlerMethod, response);
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

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
        .map(resolver -> resolver.resolveArgument(parameter, request, response)))
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
    for (ReturnValueProcessor processor : returnValueProcessors) {
      if (processor.supports(returnValue.getClass())) {
        if (processor.processReturnValue(returnValue, method, response)) {
          return; // Successfully processed the return value
        }
      }
    }

    // No suitable processor found
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

}
