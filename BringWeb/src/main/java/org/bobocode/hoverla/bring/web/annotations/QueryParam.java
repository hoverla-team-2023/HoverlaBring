package org.bobocode.hoverla.bring.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bobocode.hoverla.bring.web.servlet.resolver.QueryParamArgumentResolver;

/**
 * Annotation for indicating that a method parameter should be resolved from
 * the query parameters of an HTTP request.
 *
 * <p>Methods annotated with this can have parameters annotated with
 * {@code @QueryParam} to specify the name of the query parameter to be used for
 * resolving the parameter value.
 *
 * <p>Example:
 * <pre>
 * {@code
 * public void exampleMethod(@QueryParam("paramName") String paramValue) {
 *     // Method logic
 * }
 * }
 * </pre>
 *
 * <p>Typically used with a handler method resolver like {@link QueryParamArgumentResolver}
 * to automatically resolve and convert query parameters.
 *
 * @see QueryParamArgumentResolver
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParam {

  /**
   * The name of the query parameter for resolving the annotated parameter.
   *
   * @return The name of the query parameter.
   */
  String value();

}
