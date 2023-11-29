package org.bobocode.hoverla.bring.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used on methods within classes annotated with {@link ControllerAdvice}.
 * Indicates that the annotated method can handle a specific type of exception.
 *
 * @see ControllerAdvice
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionHandler {

  /**
   * The type of exception that the annotated method can handle.
   *
   * @return The type of exception.
   */
  Class<? extends Exception> value();

}
