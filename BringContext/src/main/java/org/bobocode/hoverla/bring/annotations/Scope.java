package org.bobocode.hoverla.bring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bobocode.hoverla.bring.bean.BeanScope;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {

  BeanScope value() default BeanScope.SINGLETON;

}
