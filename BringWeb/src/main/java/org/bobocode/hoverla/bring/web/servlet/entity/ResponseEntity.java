package org.bobocode.hoverla.bring.web.servlet.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bobocode.hoverla.bring.web.exceptions.InvalidStatusCodeException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ResponseEntity<T> {

  private static final int DEFAULT_STATUS = 200;

  private final Map<String, List<String>> headers;
  private final T body;
  private final int status;

  public ResponseEntity(T body) {
    this(body, new HashMap<>(), DEFAULT_STATUS);
  }

  public ResponseEntity(T body, Map<String, List<String>> headers) {
    this(body, headers, DEFAULT_STATUS);
  }

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

  private boolean isValidStatus(int status) {
    return status >= 100 && status <= 599;
  }

}
