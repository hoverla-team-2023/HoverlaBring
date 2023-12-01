package org.bobocode.hoverla.bring.web.exceptions;

import org.bobocode.hoverla.bring.web.annotations.PathVariable;
import org.bobocode.hoverla.bring.web.annotations.RequestMapping;

/**
 * Exception thrown when an invalid path variable is provided. This typically occurs when the value specified
 * in {@link PathVariable#value()} does not match the corresponding variable in the {@link RequestMapping#path()}.
 *
 * <p>For example:
 * <pre>
 *   {@code
 *   @RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
 *   public void invalidPathVariable(@PathVariable("my-value") String id) {
 *   }}
 * </pre>
 * In the above example, the {@link PathVariable#value()} is {@code "my-value"},
 * which does not match the {@link RequestMapping#path()} value {@code "/users/{id}"}.
 * The {@link PathVariable#value()} should be {@code "id"}.
 *
 * <p>Instances of this exception should be thrown to indicate issues related to path variable validation.
 */
public class InvalidPathVariableException extends RuntimeException {

  public InvalidPathVariableException(String message, Throwable cause) {
    super(message, cause);
  }

}
