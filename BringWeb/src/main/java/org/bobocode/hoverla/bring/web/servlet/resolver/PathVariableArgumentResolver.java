package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.lang.reflect.Parameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.annotations.PathVariable;
import org.bobocode.hoverla.bring.web.exceptions.ObjectDeserializationException;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom argument resolver for handling method parameters annotated with {@link PathVariable}.
 * Supports the resolution of various parameter types, including primitive types,
 * their wrapper classes, String, Enum, and numeric types.
 * <p>
 * Usage:
 * Annotate method parameters with {@code @PathVariable} to indicate that the value
 * should be resolved from the path variables of an HTTP request.
 * <p>
 * Example:
 */
@Slf4j
public class PathVariableArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(Parameter parameter) {
    return parameter.isAnnotationPresent(PathVariable.class);
  }

  @Override
  public Object resolveArgument(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {

    PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
    if (pathVariable == null) {
      throw new IllegalStateException("Method parameter is not annotated with @PathVariable");
    }

    String variableValue = request.getParameter(pathVariable.value());

    Class<?> parameterType = parameter.getType();
    try {
      if (String.class.isAssignableFrom(parameterType)) {
        return variableValue;
      } else if (Byte.class.isAssignableFrom(parameterType) || byte.class.isAssignableFrom(parameterType)) {
        return Byte.parseByte(variableValue);
      } else if (Short.class.isAssignableFrom(parameterType) || short.class.isAssignableFrom(parameterType)) {
        return Short.parseShort(variableValue);
      } else if (Integer.class.isAssignableFrom(parameterType) || int.class.isAssignableFrom(parameterType)) {
        return Integer.parseInt(variableValue);
      } else if (Long.class.isAssignableFrom(parameterType) || long.class.isAssignableFrom(parameterType)) {
        return Long.parseLong(variableValue);
      } else if (Float.class.isAssignableFrom(parameterType) || float.class.isAssignableFrom(parameterType)) {
        return Float.parseFloat(variableValue);
      } else if (Double.class.isAssignableFrom(parameterType) || double.class.isAssignableFrom(parameterType)) {
        return Double.parseDouble(variableValue);
      } else if (Boolean.class.isAssignableFrom(parameterType) || boolean.class.isAssignableFrom(parameterType)) {
        return Boolean.parseBoolean(variableValue);
      } else if (Enum.class.isAssignableFrom(parameterType)) {

        return Enum.valueOf((Class<Enum>) parameterType, variableValue);
      }
    } catch (IllegalArgumentException e) {
      throw new ObjectDeserializationException("Failed to parse value from path variable", e);
    }
    try {
      throw new Exception("Unsupported parameter type for @PathVariable");
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

}

