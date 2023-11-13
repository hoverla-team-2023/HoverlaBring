package org.bobocode.hoverla.bring.web;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

  private Map<Pattern, Supplier<String>> patternMap = new HashMap<>();

  public Controller() {
    patternMap.put(Pattern.compile("^/api/hello/([^/]+)/world$"), this::sayHello);

  }

  public String handleRequest(String path) {
    for (Map.Entry<Pattern, Supplier<String>> entry : patternMap.entrySet()) {
      Matcher matcher = entry.getKey().matcher(path);
      if (matcher.matches()) {

        String pathVariable = matcher.group(1);

        return entry.getValue().get();
      }
    }

    return "404 Not Found";
  }

  private String sayHello() {

    return "Hello, ";
  }
}
