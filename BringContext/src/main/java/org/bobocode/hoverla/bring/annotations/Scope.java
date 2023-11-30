package org.bobocode.hoverla.bring.annotations;

import org.bobocode.hoverla.bring.bean.BeanScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the scope of a bean marked with this annotation.
 * The {@code @Scope} annotation is used to define the lifecycle
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {

  /**
   * Specifies the scope value for the annotated bean.
   *
   * @return The scope value for the bean.
   */
  BeanScope value() default BeanScope.SINGLETON;

}
