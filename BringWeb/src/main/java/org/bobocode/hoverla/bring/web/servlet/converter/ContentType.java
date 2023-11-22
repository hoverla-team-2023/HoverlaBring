package org.bobocode.hoverla.bring.web.servlet.converter;

import lombok.Getter;

@Getter
public enum ContentType {

  APPLICATION_JSON("application/json"),
  TEXT_PLAIN("text/plain");

  private final String value;

  ContentType(String value) {this.value = value;}
}
