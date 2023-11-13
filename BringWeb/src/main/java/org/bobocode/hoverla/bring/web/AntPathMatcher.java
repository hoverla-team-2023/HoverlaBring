package org.bobocode.hoverla.bring.web;

public class AntPathMatcher {

  public boolean match(String pattern, String path) {
    String[] patternParts = tokenizePattern(pattern);
    String[] pathParts = tokenizePath(path);

    int patternIdx = 0;
    int pathIdx = 0;
    int patternWildcardIdx = -1;
    int pathWildcardIdx = -1;

    while (pathIdx < pathParts.length) {
      if (patternIdx < patternParts.length && isWildcard(patternParts[patternIdx])) {
        patternWildcardIdx = patternIdx;
        pathWildcardIdx = pathIdx;
        patternIdx++;
        pathIdx++;
      } else if (patternIdx < patternParts.length && matchTokens(patternParts[patternIdx], pathParts[pathIdx])) {
        patternIdx++;
        pathIdx++;
      } else if (patternWildcardIdx != -1) {
        patternIdx = patternWildcardIdx + 1;
        pathIdx = pathWildcardIdx + 1;
        pathWildcardIdx++;
      } else {
        return false;
      }
    }

    while (patternIdx < patternParts.length && isWildcard(patternParts[patternIdx])) {
      patternIdx++;
    }

    return patternIdx == patternParts.length;
  }

  private String[] tokenizePattern(String pattern) {
    return pattern.split("/");
  }

  private String[] tokenizePath(String path) {
    return path.split("/");
  }

  private boolean isWildcard(String token) {
    return token.equals("*") || token.equals("**") || token.startsWith("{") && token.endsWith("}");
  }

  private boolean matchTokens(String patternToken, String pathToken) {
    return patternToken.equals(pathToken) || (patternToken.equals("**") && !pathToken.isEmpty()) ||
           isPatternWithCurlyBraces(patternToken) && matchPatternWithCurlyBraces(patternToken, pathToken);
  }

  private boolean isPatternWithCurlyBraces(String token) {
    return token.startsWith("{") && token.endsWith("}");
  }

  private boolean matchPatternWithCurlyBraces(String patternToken, String pathToken) {
    String patternRegex = patternToken.substring(1, patternToken.length() - 1);
    return pathToken.matches(patternRegex);
  }

  public static void main(String[] args) {
    AntPathMatcher antPathMatcher = new AntPathMatcher();


    System.out.println(antPathMatcher.match("/hello", "/hello"));
    System.out.println(antPathMatcher.match("/hello/a?thor", "/hello/author"));
    System.out.println(antPathMatcher.match("/hello/a*thor", "/hello/athor"));
    System.out.println(antPathMatcher.match("/hello/**", "/hello/any/path"));
    System.out.println(antPathMatcher.match("/resources/{*path}", "/resources/some/path"));
    System.out.println(antPathMatcher.match("/hello/{path:[a-z]+}", "/hello/abc"));
    System.out.println(antPathMatcher.match("/hello/{path:[a-z]+}", "/hello/123"));
  }
}
