package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.bobocode.hoverla.bring.web.annotations.QueryParam;
import org.bobocode.hoverla.bring.web.exceptions.ObjectDeserializationException;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Resolves method parameters annotated with {@link QueryParam}.
 * Supports the resolution of various parameter types, including primitive types,
 * their wrapper classes, String, Enum, and collections (List, Set) of supported types.
 * <p>
 * Usage:
 * Annotate method parameters with {@code @QueryParam} to indicate that the value
 * should be resolved from the query parameters of an HTTP request.
 * <p>
 * Example:
 * {@code
 * public void exampleMethod(@QueryParam("paramName") String paramValue) {
 * // Method logic
 * }
 * }
 * <p>
 * The resolver supports various parameter types, providing automatic conversion
 * from query parameters to the specified parameter types.
 * <p>
 * Note: This resolver does not handle complex cases and assumes a simple use case.
 */
@Slf4j
public class QueryParamArgumentResolver implements HandlerMethodArgumentResolver {

  private final ObjectMapper mapper = new CsvMapper();

  @Override
  public boolean supportsParameter(Parameter parameter) {
    return parameter.isAnnotationPresent(QueryParam.class);
  }

  @Override
  public Object resolveArgument(HandlerMethod handlerMethod, Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
    QueryParam queryParamAnnotation = parameter.getAnnotation(QueryParam.class);
    String queryParamName = queryParamAnnotation.value();

    Class<?> paramType = parameter.getType();
    String[] values = request.getParameterValues(queryParamName);

    try {
      log.debug("Resolving query param value with name: {}, value: {}", queryParamName, Arrays.toString(values));
      return resolveArgument(parameter, values, paramType);
    } catch (Exception exception) {
      throw new ObjectDeserializationException("Cannot deserialize query param with name: %s".formatted(queryParamName), exception);
    }
  }

  private Object resolveArgument(Parameter parameter, String[] values, Class<?> paramType) {
    if (ArrayUtils.isEmpty(values) && paramType == String.class) {
      return null;
    }

    String queryParamValues = Arrays.stream(ArrayUtils.nullToEmpty(values))
      .flatMap(value -> Arrays.stream(value.split(",")))
      .filter(Objects::nonNull)
      .collect(Collectors.joining(","));
    if (!paramType.isPrimitive() && queryParamValues.isBlank()) {
      return null;
    } else if (Collection.class.isAssignableFrom(paramType)) {
      return resolveCollectionParameter(parameter, queryParamValues);
    } else {
      return parseParameter(paramType, queryParamValues);
    }
  }

  private Object parseParameter(Class<?> targetType, String paramValue) {
    if (ClassUtils.isPrimitiveOrWrapper(targetType) || targetType == String.class) {
      return parseType(targetType, paramValue);
    } else if (targetType.isEnum()) {
      return Enum.valueOf((Class<Enum>) targetType, paramValue);
    } else {
      log.warn("Unsupported target type {} for resolving query param value {}", targetType.getTypeName(), paramValue);
      return null;
    }
  }

  private Object parseType(Class<?> targetType, String paramValue) {
    if (targetType == String.class) {
      return paramValue;
    } else if (targetType == boolean.class || targetType == Boolean.class) {
      return Boolean.parseBoolean(paramValue);
    } else if (targetType == short.class || targetType == Short.class) {
      return Short.parseShort(paramValue);
    } else if (targetType == int.class || targetType == Integer.class) {
      return Integer.parseInt(paramValue);
    } else if (targetType == long.class || targetType == Long.class) {
      return Long.parseLong(paramValue);
    } else if (targetType == double.class || targetType == Double.class) {
      return Double.parseDouble(paramValue);
    } else if (targetType == float.class || targetType == Float.class) {
      return Float.parseFloat(paramValue);
    }

    return null;
  }

  private Object resolveCollectionParameter(Parameter parameter, String values) {
    try {
      return mapper.readValue(values, mapper.getTypeFactory().constructType(parameter.getParameterizedType()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

}

