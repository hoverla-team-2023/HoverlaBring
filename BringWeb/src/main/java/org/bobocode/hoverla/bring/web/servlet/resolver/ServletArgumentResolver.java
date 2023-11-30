package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.bobocode.hoverla.bring.web.exceptions.UnsupportedArgumentException;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.nonNull;

/**
 * The ServletArgumentResolver class is a custom implementation of the {@link HandlerMethodArgumentResolver} interface.
 * It resolves the {@link HttpServletRequest}, {@link HttpServletResponse}, {@link HttpSession}, {@link ServletContext}, and {@link Cookie} objects as
 * method arguments in a controller method.
 * To include {@link Cookie cookies} as method arguments in a controller method, use either <code>Cookie[]</code>, <code>List&lt;Cookie&gt;</code>,
 * or <code>Set&lt;Cookie&gt;</code>
 **/
@Slf4j
public class ServletArgumentResolver implements HandlerMethodArgumentResolver {

  // Map for supported type. It contains the predicate for each type and the corresponding argument resolver
  private final Map<Predicate<Parameter>, ArgumentResolver> supportedArgumentResolvers = Map.of(
    this::isHttpServletRequest, this::resolveHttpServletRequest,
    this::isHttpServletResponse, this::resolveHttpServletResponse,
    this::isHttpSession, this::resolveHttpSession,
    this::isServletContext, this::resolveServletContext,
    this::isCookie, this::resolveCookie
  );

  /**
   * Returns true if the given parameter is of type HttpServletRequest or HttpServletResponse, false otherwise.
   *
   * @param parameter the parameter to be checked
   *
   * @return true if the given parameter is of type HttpServletRequest or HttpServletResponse, false otherwise
   */
  @Override
  public boolean supportsParameter(Parameter parameter) {
    return nonNull(parameter) && supportedArgumentResolvers.keySet().stream()
      .anyMatch(predicate -> predicate.test(parameter));
  }

  /**
   * Resolves the HttpServletRequest or HttpServletResponse object as a method argument in a controller method.
   *
   * @param parameter the parameter to be resolved
   * @param request   the HttpServletRequest object
   * @param response  the HttpServletResponse object
   *
   * @return the resolved argument
   */
  @Override
  public Object resolveArgument(HandlerMethod handlerMethod, Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
    return supportedArgumentResolvers.entrySet().stream()
      .filter(entry -> entry.getKey().test(parameter))
      .findFirst()
      .map(Map.Entry::getValue)
      .map(argumentResolver -> {
        log.debug("Found argument resolver for type {}", parameter.getParameterizedType().getTypeName());
        return argumentResolver.resolveArgument(parameter, request, response);
      })
      .orElseThrow(() -> {
        var message = String.format(
          "Unsupported parameter type: %s. The supported parameter types are HttpServletRequest, HttpServletResponse, HttpSession, ServletContext, List<Cookie>",
          parameter.getType()
        );

        log.error(message);
        return new UnsupportedArgumentException(message);
      });
  }

  /**
   * Resolves the argument object for a controller method based on the parameter, request, response.
   */
  @FunctionalInterface
  private interface ArgumentResolver {

    Object resolveArgument(Parameter parameter, HttpServletRequest request, HttpServletResponse response);

  }

  private boolean isHttpServletRequest(Parameter parameter) {
    return HttpServletRequest.class.isAssignableFrom(parameter.getType());
  }

  private HttpServletRequest resolveHttpServletRequest(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
    return request;
  }

  private boolean isHttpServletResponse(Parameter parameter) {
    return HttpServletResponse.class.isAssignableFrom(parameter.getType());
  }

  private HttpServletResponse resolveHttpServletResponse(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
    return response;
  }

  private boolean isHttpSession(Parameter parameter) {
    return HttpSession.class.isAssignableFrom(parameter.getType());
  }

  private HttpSession resolveHttpSession(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
    return request.getSession();
  }

  private boolean isServletContext(Parameter parameter) {
    return ServletContext.class.isAssignableFrom(parameter.getType());
  }

  private ServletContext resolveServletContext(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
    return request.getServletContext();
  }

  private boolean isCookie(Parameter parameter) {
    return Cookie[].class.isAssignableFrom(parameter.getType()) || isCookieCollection(parameter);
  }

  private boolean isCookieCollection(Parameter parameter) {
    return Collection.class.isAssignableFrom(parameter.getType()) && Cookie.class == getGenericType(parameter);
  }

  private Object resolveCookie(Parameter parameter, HttpServletRequest request, HttpServletResponse response) {
    Class<?> type = parameter.getType();
    Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElseGet(() -> new Cookie[0]);
    if (Cookie[].class.isAssignableFrom(type)) {
      return cookies;
    }

    List<Cookie> cookieList = List.of(cookies);
    if (List.class.isAssignableFrom(type)) {
      return cookieList;
    }
    if (Set.class.isAssignableFrom(type)) {
      return new HashSet<>(cookieList);
    }

    String message = "Unsupported Cookie parameter type: %s. Consider using List<Cookie>, or Cookie[] as a method argument".formatted(parameter.getType());

    log.error(message);
    throw new UnsupportedArgumentException(message);
  }

  private Type getGenericType(Parameter parameter) {
    var type = parameter.getParameterizedType();
    if (type instanceof ParameterizedType parameterizedType) {
      return parameterizedType.getActualTypeArguments()[0];
    }

    return null;
  }

}
