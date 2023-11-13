package org.bobocode.hoverla.bring.web;

import java.util.regex.Pattern;

public class AntPathMatcherTest {

  private String convert(String antPattern) {
    StringBuilder pattern = new StringBuilder();
    boolean inVar = false;

    for (char ch : antPattern.toCharArray()) {
      if (inVar) {
        if (ch == '}') {
          pattern.append("[^/]*");
          inVar = false;
        }
      } else {
        if (ch == '?') {
          pattern.append('.');
        } else if (ch == '*') {
          pattern.append(".*");
        } else if (ch == '{') {
          inVar = true;
        } else if (ch == '/') {
          pattern.append('/');
        } else {
          pattern.append(Pattern.quote(String.valueOf(ch)));
        }
      }
    }

    return pattern.toString();
  }

  public boolean match(String antPattern, String str) {
    Pattern pattern = Pattern.compile(convert(antPattern));
    return pattern.matcher(str).matches();
  }
}
