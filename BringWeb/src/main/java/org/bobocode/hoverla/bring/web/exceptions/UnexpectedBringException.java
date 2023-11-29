package org.bobocode.hoverla.bring.web.exceptions;

/**
 * Exception thrown to indicate an unexpected error in the Bring application.
 * This runtime exception is used when an unexpected or unrecoverable situation occurs during the execution
 * of the Bring application that is not covered by more specific exception types.
 *
 * <p>It is recommended to review the logs and details of this exception to identify and address the root cause of the issue.
 */
public class UnexpectedBringException extends RuntimeException {

  public UnexpectedBringException(String message, Throwable cause) {
    super(message, cause);
  }

}
