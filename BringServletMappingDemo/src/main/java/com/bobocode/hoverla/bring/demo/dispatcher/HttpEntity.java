package com.bobocode.hoverla.bring.demo.dispatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpEntity<T> {

  private Map<String, List<String>> headers;
  private T body;

  public HttpEntity(T body) {
    this.body = body;
    this.headers = new HashMap<>();
  }

  public HttpEntity(T body, Map<String, List<String>> headers) {
    this.body = body;
    this.headers = headers;
  }

  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  public T getBody() {
    return body;
  }
}
