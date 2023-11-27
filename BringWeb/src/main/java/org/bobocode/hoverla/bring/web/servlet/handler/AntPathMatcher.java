package org.bobocode.hoverla.bring.web.servlet.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to match HTTP request paths against patterns.
 * It supports Ant-style path patterns with wildcards and curly braces.
 *
 * Ant-style path patterns are defined as follows:
 * - "*" matches zero or more characters
 * - "**" matches zero or more directories in a path
 * - "{variableName}" matches a single path segment
 *
 * For example, the pattern "/user/{username}" would match "/user/john" and "/user/abdul", "/user/john/details".
 */

@Slf4j
public class AntPathMatcher {

  /**
   * Matches the given path against the given pattern.
   *
   * @param pattern the pattern to match against
   * @param path    the path to match
   *
   * @return true if the path matches the pattern, false otherwise
   */

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

  /**
   * Tokenizes the given pattern into its constituent parts.
   *
   * @param pattern the pattern to tokenize
   *
   * @return an array of the pattern's constituent parts
   */

  private String[] tokenizePattern(String pattern) {
    return pattern.split("/");
  }

  /**
   * Tokenizes the given path into its constituent parts.
   *
   * @param path the path to tokenize
   *
   * @return an array of the path's constituent parts
   */
  private String[] tokenizePath(String path) {
    return path.split("/");
  }

  /**
   * Checks if the given token is a wildcard.
   *
   * @param token the token to check
   *
   * @return true if the token is a wildcard, false otherwise
   */
  private boolean isWildcard(String token) {
    return token.equals("*") || token.equals("**") || token.startsWith("{") && token.endsWith("}");
  }

  /**
   * Matches the given tokens against each other.
   *
   * @param patternToken the pattern token to match
   * @param pathToken    the path token to match
   *
   * @return true if the tokens match, false otherwise
   */
  private boolean matchTokens(String patternToken, String pathToken) {
    return patternToken.equals(pathToken) || (patternToken.equals("**") && !pathToken.isEmpty()) ||
           isPatternWithCurlyBraces(patternToken) && matchPatternWithCurlyBraces(patternToken, pathToken);
  }

  /**
   * Checks if the given token is a pattern with curly braces.
   *
   * @param token the token to check
   *
   * @return true if the token is a pattern with curly braces, false otherwise
   */
  private boolean isPatternWithCurlyBraces(String token) {
    return token.startsWith("{") && token.endsWith("}");
  }

  /**
   * Matches the given pattern token with curly braces against the given path token.
   *
   * @param patternToken the pattern token to match
   * @param pathToken    the path token to match
   *
   * @return true if the tokens match, false otherwise
   */
  private boolean matchPatternWithCurlyBraces(String patternToken, String pathToken) {
    String patternRegex = patternToken.substring(1, patternToken.length() - 1);
    return pathToken.matches(patternRegex);
  }

}