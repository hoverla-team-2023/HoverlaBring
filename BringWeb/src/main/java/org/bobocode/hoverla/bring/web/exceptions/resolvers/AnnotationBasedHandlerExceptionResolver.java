package org.bobocode.hoverla.bring.web.exceptions.resolvers;

import lombok.extern.slf4j.Slf4j;
import org.bobocode.hoverla.bring.web.exceptions.RegisteringHandlerExceptionMethodException;
import org.bobocode.hoverla.bring.web.annotations.ControllerAdvice;
import org.bobocode.hoverla.bring.web.annotations.ExceptionHandler;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of {@code HandlerExceptionResolver} that resolves exceptions using methods annotated with
 * {@code @ExceptionHandler} in classes annotated with {@code @ControllerAdvice}.
 *
 * <p>This exception resolver is designed to handle exceptions in a flexible and customizable manner by relying on
 * exception handler methods defined in controller advice classes. Controller advice classes can be annotated with
 * {@code @ControllerAdvice}, and exception handler methods within those classes are annotated with {@code @ExceptionHandler}.
 *
 * <p>The exception resolution process involves matching the thrown exception type against registered exception handler
 * methods. If an exact match is found, the corresponding handler method is used. If not, the hierarchy depth of the
 * exception type is calculated to find the closest assignable exception type, and the corresponding handler method is used.
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
      if (isControllerAdvice(controllerAdvice)) {
        Class<?> adviceClass = controllerAdvice.getClass();
        for (Method method : adviceClass.getDeclaredMethods()) {
          if (isMethodMarkedAsExceptionHandler(method)) {
            registerExceptionHandler(controllerAdvice, adviceClass, method);
          }
        }
      }
    }
  }

  /***
   * <p>This method checks whether the exception resolver has a registered handler method for the specified exception type
   * or its subtypes. If a handler method is found, the resolver is considered capable of handling exceptions of that type.
   *
   * @param clazz The {@code Class} object representing the type of the exception.
   * @return {@code true} if this resolver can handle exceptions of the given type or its subtypes; {@code false} otherwise.
   */
  @Override
  public boolean canHandle(Class<? extends Exception> clazz) {
    return exceptionHandlers.keySet()
      .stream()
      .anyMatch(exceptionType -> exceptionType.isAssignableFrom(clazz));
  }

  /**
   * Resolves a handler method for the given exception by checking registered exception handlers.
   *
   * <p>If a handler method specifically designed for the exception type is found, it is returned.
   * Otherwise, the resolver searches for a handler that can handle a superclass of the exception type,
   * and returns the handler method for the most specific superclass.
   *
   * @param exception The exception for which a handler method is to be resolved.
   *
   * @return The resolved {@code HandlerMethod} or {@code null} if no suitable handler is found.
   */
  @Override
  public HandlerMethod resolveException(Exception exception) {
    Class<? extends Exception> exceptionType = exception.getClass();

    HandlerMethod handlerMethod = exceptionHandlers.get(exceptionType);
    if (handlerMethod != null) {
      return handlerMethod;
    }

    return exceptionHandlers.keySet().stream()
      .filter(type -> type.isAssignableFrom(exceptionType))
      .min(Comparator.comparingInt(type -> calculateHierarchyDepth(type, exceptionType)))
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

  private void registerExceptionHandler(Object controllerAdvice, Class<?> adviceClass, Method method) {
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

    validateExceptionMethodArguments(method, exceptionType, adviceClass);

    exceptionHandlers.put(exceptionType, handlerMethod);
  }

  private void validateExceptionMethodArguments(Method method, Class<? extends Exception> exceptionType, Class<?> adviceClass) {
    Parameter[] parameters = method.getParameters();
    if (parameters.length != 1) {
      throw new RegisteringHandlerExceptionMethodException(
        "Exception handler methods must have exactly one parameter (method: %s advice: %s).".formatted(method.getName(), adviceClass.getName()));
    }
    if (!parameters[0].getType().isAssignableFrom(exceptionType)) {
      throw new RegisteringHandlerExceptionMethodException(
        "The parameter type of the exception handler method must match the specified exception type (method: %s advice: %s).".formatted(method.getName(),
                                                                                                                                        adviceClass.getName()));
    }
  }

  private boolean isMethodMarkedAsExceptionHandler(Method method) {
    return method.isAnnotationPresent(ExceptionHandler.class);
  }

  private boolean isControllerAdvice(Object controllerAdvice) {
    return controllerAdvice.getClass().isAnnotationPresent(ControllerAdvice.class);
  }

}
