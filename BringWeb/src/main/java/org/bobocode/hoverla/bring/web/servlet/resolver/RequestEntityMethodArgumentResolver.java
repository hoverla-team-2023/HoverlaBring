package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.servlet.converter.HttpMessageConverter;
import org.bobocode.hoverla.bring.web.servlet.entity.RequestEntity;

import lombok.extern.log4j.Log4j2;

/**
 * Supports {@link RequestEntity}
 */
@Log4j2
public class RequestEntityMethodArgumentResolver extends HttpMessageConverterDelegatingArgumentResolver {

  public RequestEntityMethodArgumentResolver(List<HttpMessageConverter> messageConverters) {
    super(messageConverters);
  }

  @Override
  public boolean supportsParameter(Parameter parameter) {
    return RequestEntity.class.isAssignableFrom(parameter.getType());
  }

  @Override
  public Object resolveArgument(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
    var genericType = getGenericType(parameter);
    log.debug("Converting request body to {}", genericType);

    var body = readRequestBody(genericType, request);
    return new RequestEntity<>(getHeaders(request), body);
  }

  private Type getGenericType(Parameter parameter) {
    var type = parameter.getParameterizedType();
    if (type instanceof ParameterizedType parameterizedType) {
      return parameterizedType.getActualTypeArguments()[0];
    }

    log.info("No generic type found for {}. Consider using RequestEntity<T> instead", parameter.getType().getSimpleName());
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
