package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.lang.reflect.Parameter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.annotations.PathVariable;
import org.bobocode.hoverla.bring.web.exceptions.InvalidPathVariableException;
import org.bobocode.hoverla.bring.web.exceptions.ObjectDeserializationException;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;
import org.bobocode.hoverla.bring.web.util.TypeUtils;

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
  public Object resolveArgument(HandlerMethod handlerMethod, Parameter parameter, HttpServletRequest request, HttpServletResponse response) {

    PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
    if (pathVariable == null) {
      throw new IllegalStateException("Method parameter is not annotated with @PathVariable");
    }

    String requestURI = request.getRequestURI();

    String variableValue = extractPathVariableValue(handlerMethod, requestURI, pathVariable.value());

    Class<?> parameterType = parameter.getType();
    try {
      log.debug("Resolving path variable param value with requestURI: {} , value: {}", requestURI, variableValue);
      return TypeUtils.parseType(parameterType, variableValue);
    } catch (IllegalArgumentException e) {
      throw new ObjectDeserializationException("Failed to parse value from path variable", e);
    }
  }

  private String extractPathVariableValue(HandlerMethod handlerMethod, String path, String variableName) {
    String methodPattern = handlerMethod.getPath();

    String regex = methodPattern.replaceAll("\\{([^/]+)}", "(?<$1>[^/]+)");
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(path);

    if (matcher.find()) {
      try {
        return matcher.group(variableName);
      } catch (IllegalArgumentException e) {
        throw new InvalidPathVariableException("Failed to extract value for the path variable: " + variableName, e);
      }
    }

    return null;
  }

}

