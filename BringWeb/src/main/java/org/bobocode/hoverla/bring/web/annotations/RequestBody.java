package org.bobocode.hoverla.bring.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate that the annotated method parameter is a {@link org.bobocode.hoverla.bring.web.servlet.DispatcherServlet servlet}
 * request body and should be mapped to the parameter class. The request body should be sent with the content type <code>application/json</code>.
 * <p>Example:</p>
 * <pre>
 * class MyController {
 *
 *      &#64;RequestMapping(path = "/api/process-entity", method = RequestMethod.POST)
 *      public ResponseEntity&lt;MyDto&gt; processEntity(&#64;RequestBody&lt;MyDto&gt; body) {
 *        // implementation
 *      }
 *   }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestBody {
}
