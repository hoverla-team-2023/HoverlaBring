package org.bobocode.hoverla.bring.web.servlet.mapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.bobocode.hoverla.bring.web.annotations.RequestMapping;
import org.bobocode.hoverla.bring.web.annotations.RequestMethod;
import org.bobocode.hoverla.bring.web.servlet.handler.AntPathMatcher;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

import lombok.extern.slf4j.Slf4j;

/**
 * AnnotationBasedHandlerMapping is a class that maps HTTP requests to handler methods based on annotations.
 * It uses reflection to find all methods in the specified package that are annotated with the specified annotation.
 * The mapping between HTTP requests and handler methods is based on the value of the specified annotation.
 */
@Slf4j
public class AnnotationBasedHandlerMapping implements HandlerMapping {

  /**
   * A map that stores the handler methods based on their path.
   */
  private final Map<String, HandlerMethod> handlerMethods = new HashMap<>();
  /**
   * An instance of AntPathMatcher used to match the request path against the handler method paths.
   */
  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  /**
   * Constructs a new instance of the AnnotationBasedHandlerMapping class and initializes the handler methods map.
   * It uses reflection to find all methods in the specified package that are annotated with the specified annotation.
   * The mapping between HTTP requests and handler methods is based on the value of the specified annotation.
   *
   * @param controllers an array of controller objects
   */
  public AnnotationBasedHandlerMapping(Object... controllers) {
    for (Object controller : controllers) {
      for (Method method : controller.getClass().getMethods()) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
          RequestMapping mapping = method.getAnnotation(RequestMapping.class);
          String path = mapping.path();
          RequestMethod requestMethod = mapping.method();
          handlerMethods.put(path + ":" + requestMethod.name(), new HandlerMethod(controller.getClass(), method, path, method.getParameters(), controller, requestMethod));
        }
      }
    }
  }

  /**
   * Gets the handler method that matches the given request.
   * It iterates over the handler methods and checks if the request path matches the path of the handler method.
   * If a match is found, it returns the handler method; otherwise, it returns null.
   *
   * @param request the HTTP request to match
   *
   * @return the handler method that matches the request, or null if no match is found
   */
  @Override
  public HandlerMethod getHandlerMethod(HttpServletRequest request) {
    String requestPath = request.getRequestURI();
    RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
    for (Map.Entry<String, HandlerMethod> entry : handlerMethods.entrySet()) {
      HandlerMethod handlerMethod = entry.getValue();
      if (pathMatcher.match(handlerMethod.getPath() + ":" + handlerMethod.getMethod().getName(), requestPath + ":" + requestMethod.name())) {
        return handlerMethod;
      }
    }
    return null;
  }

}
