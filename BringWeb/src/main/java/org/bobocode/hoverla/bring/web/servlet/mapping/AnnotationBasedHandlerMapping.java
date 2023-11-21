package org.bobocode.hoverla.bring.web.servlet.mapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.bobocode.hoverla.bring.web.annotations.RequestMapping;
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
   * Maps HTTP requests to handler methods based on the specified annotation.
   */
  private final Map<String, HandlerMethod> handlerMethods = new HashMap<>();

  /**
   * Constructs a new instance of the AnnotationBasedHandlerMapping class and initializes the handler methods map.
   *
   * @param controllers an array of controller objects
   */
  public AnnotationBasedHandlerMapping(Object... controllers) {
    for (Object controller : controllers) {
      for (Method method : controller.getClass().getMethods()) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
          RequestMapping mapping = method.getAnnotation(RequestMapping.class);
          String path = mapping.path();
          handlerMethods.put(path, new HandlerMethod(controller.getClass(), method, path, method.getParameters(), controller));
        }
      }
    }
  }

  @Override
  public HandlerMethod getHandlerMethod(HttpServletRequest request) {
    return handlerMethods.get(request.getPathInfo());
  }

}
