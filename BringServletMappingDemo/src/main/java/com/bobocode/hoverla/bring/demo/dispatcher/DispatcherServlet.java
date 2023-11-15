package com.bobocode.hoverla.bring.demo.dispatcher;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.bobocode.hoverla.bring.demo.dispatcher.converters.HttpMessageConverter;
import com.bobocode.hoverla.bring.demo.dispatcher.converters.JsonHttpMessageConverter;
import com.bobocode.hoverla.bring.demo.dispatcher.converters.TextPlainHttpMessageConverter;
import com.bobocode.hoverla.bring.demo.dispatcher.mapping.Handler;
import com.bobocode.hoverla.bring.demo.dispatcher.mapping.SimpleHandlerMapping;
import com.bobocode.hoverla.bring.demo.dispatcher.processors.HttpEntityReturnValueProcessor;
import com.bobocode.hoverla.bring.demo.dispatcher.processors.ReturnValueProcessor;
import com.bobocode.hoverla.bring.demo.dispatcher.processors.StringReturnValueProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

  private List<ReturnValueProcessor> returnValueProcessors;
  private SimpleHandlerMapping handlerMapping;
  List<HttpMessageConverter> converters;

  public DispatcherServlet() {
  }

  @Override
  public void init() {
    handlerMapping = new SimpleHandlerMapping();
    handlerMapping.scanControllers("com.bobocode.hoverla.bring.demo.controller");
    converters = new ArrayList<>();
    converters.add(new TextPlainHttpMessageConverter());
    converters.add(new JsonHttpMessageConverter(new ObjectMapper()));

    returnValueProcessors = new ArrayList<>();
    returnValueProcessors.add(new HttpEntityReturnValueProcessor(converters));
    returnValueProcessors.add(new StringReturnValueProcessor(converters));
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    processRequest(request, response);
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String requestURI = request.getRequestURI();
    Handler handler = handlerMapping.getHandler(requestURI);

    if (handler != null) {
      // Invoke the handler method
      Object returnValue = handler.handleRequest(request, response);

      // Process the return value
      processReturnValue(returnValue, handler.getMethod(), request, response);
    } else {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private void processReturnValue(Object returnValue, Method method, HttpServletRequest request, HttpServletResponse response) throws IOException {
    for (ReturnValueProcessor processor : returnValueProcessors) {
      if (processor.supports(returnValue.getClass())) {
        if (processor.processReturnValue(returnValue, method, request, response)) {
          return; // Successfully processed the return value
        }
      }
    }

    // No suitable processor found
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

}


