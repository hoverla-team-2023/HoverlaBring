package org.bobocode.hoverla.bring.web.exceptions.resolvers;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.bobocode.hoverla.bring.web.annotations.ControllerAdvice;
import org.bobocode.hoverla.bring.web.annotations.ExceptionHandler;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

import lombok.extern.slf4j.Slf4j;

/**
 * An implementation of {@code HandlerExceptionResolver} that resolves exceptions using methods annotated with
 * {@code @ExceptionHandler} in classes annotated with {@code @ControllerAdvice}.
 */
@Slf4j
public class AnnotationBasedHandlerExceptionResolver implements HandlerExceptionResolver {

  private final Map<Class<? extends Exception>, HandlerMethod> exceptionHandlers = new HashMap<>();

  public AnnotationBasedHandlerExceptionResolver(Object[] controllerAdvices) {
    buildExceptionHandlers(controllerAdvices);
  }

  //todo integrate with IoC
  private void buildExceptionHandlers(Object[] controllerAdvices) {
    for (Object controllerAdvice : controllerAdvices) {
      if (controllerAdvice.getClass().isAnnotationPresent(ControllerAdvice.class)) {
        Class<?> adviceClass = controllerAdvice.getClass();
        for (Method method : adviceClass.getDeclaredMethods()) {
          if (method.isAnnotationPresent(ExceptionHandler.class)) {
            Class<? extends Exception> exceptionType = method.getAnnotation(ExceptionHandler.class).value();
            HandlerMethod handlerMethod =
              HandlerMethod.builder()
                .method(method)
                .bean(controllerAdvice)
                .beanType(adviceClass)
                .parameters(method.getParameters())
                .build();

            if (exceptionHandlers.containsKey(exceptionType)) {
              log.warn("Overriding existing ExceptionHandler for type: {}", exceptionType);
            }
            exceptionHandlers.put(exceptionType, handlerMethod);
          }
        }
      }
    }
  }

  @Override
  public boolean canHandle(Class<? extends Exception> clazz) {
    return exceptionHandlers.keySet()
      .stream()
      .anyMatch(exceptionType -> exceptionType.isAssignableFrom(clazz));
  }

  @Override
  public HandlerMethod resolveException(Exception exception) {
    Class<? extends Exception> exceptionType = exception.getClass();

    HandlerMethod handlerMethod = exceptionHandlers.get(exceptionType);
    if (handlerMethod != null) {
      return handlerMethod;
    }

    var classStream = exceptionHandlers.keySet().stream()
      .filter(type -> type.isAssignableFrom(exceptionType)).toList();

    var min = classStream.stream()
      .min(Comparator.comparingInt(type -> calculateHierarchyDepth(type, exceptionType)));

    return min
      .map(exceptionHandlers::get)
      .orElse(null);
  }

  public int calculateHierarchyDepth(Class<?> type, Class<?> target) {
    int depth = 0;
    Class<?> superClass = target;
    while (superClass != null && !type.equals(superClass)) {
      superClass = superClass.getSuperclass();
      depth++;
    }
    return depth;
  }

}
