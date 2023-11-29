package org.bobocode.hoverla.bring.web.servlet.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class AntPathMatcherTest {

  private AntPathMatcher matcher;

  @BeforeEach
  public void setup() {
    matcher = new AntPathMatcher();
  }

  @Test
  public void testMatchExactPattern() {
    assertTrue(matcher.match("/user/{username}", "/user/john"));
    assertFalse(matcher.match("/user/{username}", "/user/john/details"));
  }

  @Test
  public void testMatchWildcardPattern() {
    assertTrue(matcher.match("/user/*", "/user/john"));
  }

  @Test
  public void testMatchMixedPattern() {
    assertTrue(matcher.match("/user/{username}/details", "/user/john/details"));
  }

  @Test
  public void testEmptyPattern() {
    assertTrue(matcher.match("", ""));
  }

  @Test
  public void testEmptyPath() {
    assertFalse(matcher.match("/user/{username}", ""));
  }

  @Test
  public void testTokenizePattern() {
    String[] parts = matcher.tokenizePattern("/user/{username}/details");
    assertArrayEquals(new String[] { "", "user", "{username}", "details" }, parts);
  }

  @Test
  public void testTokenizePath() {
    String[] parts = matcher.tokenizePath("/user/john/details");
    assertArrayEquals(new String[] { "", "user", "john", "details" }, parts);
  }

  @Test
  public void testIsWildcard() {
    assertTrue(matcher.isWildcard("*"));
    assertTrue(matcher.isWildcard("{variable}"));
    assertFalse(matcher.isWildcard("user"));
  }

  @Test
  public void testMatchTokens() {
    assertTrue(matcher.matchTokens("user", "user"));
    assertFalse(matcher.matchTokens("{variable}", "user"));
  }

  @Test
  public void testIsPatternWithCurlyBraces() {
    assertTrue(matcher.isPatternWithCurlyBraces("{variable}"));
    assertFalse(matcher.isPatternWithCurlyBraces("user"));
  }

  @Test
  public void testMatchPatternWithCurlyBraces() {
    assertTrue(matcher.matchPatternWithCurlyBraces("{\\d+}", "123"));
    assertFalse(matcher.matchPatternWithCurlyBraces("{\\d+}", "abc"));
  }

}
