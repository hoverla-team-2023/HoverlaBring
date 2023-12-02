package org.bobocode.hoverla.bring.web.servlet.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bobocode.hoverla.bring.web.exceptions.InvalidStatusCodeException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * This class represents a response entity that contains a body, headers, and a status code.
 * The status code should be between 100 and 599, and if an invalid status code is provided, an InvalidStatusCodeException will be thrown.
 *
 * @param <T> the type of the response body
 */
@Slf4j
@Getter
public class ResponseEntity<T> {

  private static final int DEFAULT_STATUS = 200;

  private final Map<String, List<String>> headers;
  private final T body;
  private final int status;

  /**
   * Constructor for the response entity with only the body.
   *
   * @param body the response body
   */
  public ResponseEntity(T body) {
    this(body, new HashMap<>(), DEFAULT_STATUS);
  }

  /**
   * Constructor for the response entity with the body and headers.
   *
   * @param body    the response body
   * @param headers the response headers
   */
  public ResponseEntity(T body, Map<String, List<String>> headers) {
    this(body, headers, DEFAULT_STATUS);
  }

  /**
   * Constructor for the response entity with the body, headers, and status code.
   *
   * @param body    the response body
   * @param headers the response headers
   * @param status  the response status code
   */
  public ResponseEntity(T body, Map<String, List<String>> headers, int status) {
    if (!isValidStatus(status)) {
      var message = "Invalid provided status code: %d. A valid status code should be between 100 and 599".formatted(status);
      log.error(message);
      throw new InvalidStatusCodeException(message);
    }

    this.body = body;
    this.headers = headers;
    this.status = status;
  }

  /**
   * Checks if the provided status code is valid.
   *
   * @param status the status code to check
   *
   * @return true if the status code is valid, false otherwise
   */
  private boolean isValidStatus(int status) {
    return status >= 100 && status <= 599;
  }

}