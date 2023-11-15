package org.bobocode.hoverla.bring.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate that the annotated method parameter is a {@link org.bobocode.hoverla.bring.web.servlet.DispatcherServlet servlet}
 * request body and should be mapped to the parameter class
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestBody {
}
