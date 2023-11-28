package org.bobocode.hoverla.bring.web.servlet.entity;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestEntity<T> {

  private final Map<String, List<String>> headers;
  private final T body;

}
