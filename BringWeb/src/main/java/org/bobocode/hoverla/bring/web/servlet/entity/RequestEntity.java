package org.bobocode.hoverla.bring.web.servlet.entity;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class represents an HTTP request entity with headers and a body.
 *
 * @param <T> the type of the request body
 */
@Getter
@RequiredArgsConstructor
public class RequestEntity<T> {

  /**
   * The headers of the request.
   */
  private final Map<String, List<String>> headers;

  /**
   * The body of the request.
   */
  private final T body;

}
