package org.bobocode.hoverla.bring.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the path to the controller method.
 * <p>
 * Please note that current annotation should be specified on methods of a class annotated with {@link Controller}.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

  /**
   * Alias for {@link #path()}
   */
  String value();

  String path();

  /**
   * Specifies the HTTP method to use for this request.
   *
   * @return HTTP method
   */
  RequestMethod method();

}
