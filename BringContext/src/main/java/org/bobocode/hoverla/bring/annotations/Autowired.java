package org.bobocode.hoverla.bring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Fields annotated with {@code @Autowired} will be automatically
 * populated by the Bring IoC container, resolving the corresponding dependency.
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}
