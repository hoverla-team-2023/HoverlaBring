package org.bobocode.hoverla.bring.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bobocode.hoverla.bring.annotations.Component;

/**
 * Use this annotation to mark a class as a controller.
 *
 * @see RequestMapping
 * @see RequestBody
 * @see ResponseBody
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Controller {
}
