package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.servlet.entity.RequestEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Supports {@link RequestEntity}
 * An array of POJOs, collection/array of enums, bounded wildcards are not supported
 */
@Log4j2
@RequiredArgsConstructor
public class RequestEntityMethodArgumentResolver implements HandlerMethodArgumentResolver {

  private final ObjectMapper objectMapper;

  @Override
  public boolean supportsParameter(Parameter parameter) {
    return RequestEntity.class.isAssignableFrom(parameter.getType());
  }

  @Override
  public Object resolveArgument(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
    var genericType = getGenericType(parameter);
    final Object body;

    try {
      body = objectMapper.readValue(request.getInputStream(), objectMapper.constructType(genericType));
    } catch (IOException e) {
      var message = "Failed to convert the request body to the required type %s".formatted(parameter.getType().getSimpleName());
      log.error(message, e);
      // TODO: 24.11.2023 throw ObjectDeserializationException instead
      throw new RuntimeException(message, e);
    }

    return new RequestEntity<>(getHeaders(request), body);
  }

  private Type getGenericType(Parameter parameter) {
    var type = parameter.getParameterizedType();
    if (type instanceof ParameterizedType parameterizedType) {
      return parameterizedType.getActualTypeArguments()[0];
    }

    log.info(String.format(
      "No generic type found for %s. Use RequestEntity<T>. Note that bounded wildcards, enum arrays/collections, POJO arrays are not supported",
      parameter.getType().getSimpleName()
    ));
    return Object.class;
  }

  private Map<String, List<String>> getHeaders(HttpServletRequest request) {
    Map<String, List<String>> headers = new HashMap<>();

    for (var headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
      var header = headerNames.nextElement();
      for (var values = request.getHeaders(header); values.hasMoreElements(); ) {
        var headerValues = new ArrayList<String>();
        headerValues.add(values.nextElement());

        headers.computeIfPresent(header, (k, v) -> {
          v.addAll(headerValues);
          return v;
        });
        headers.putIfAbsent(header, headerValues);
      }
    }

    return headers;
  }

}
