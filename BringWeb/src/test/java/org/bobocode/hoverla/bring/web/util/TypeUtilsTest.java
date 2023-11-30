package org.bobocode.hoverla.bring.web.util;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TypeUtilsTest {

  static Stream<Arguments> textPlainTypes() {
    return Stream.of(
      arguments(String.class),
      arguments(Integer.class),
      arguments(int.class),
      arguments(Double.class),
      arguments(double.class),
      arguments(Long.class),
      arguments(long.class),
      arguments(Boolean.class),
      arguments(boolean.class),
      arguments(Character.class),
      arguments(char.class)
    );
  }

  @ParameterizedTest
  @MethodSource("textPlainTypes")
  void givenIsTextPlainType_whenIsTextPlainType_thenTrue(Class<?> type) {
    var result = TypeUtils.isTextPlainType(type);
    assertTrue(result);
  }

  @Test
  void givenIsObjectType_whenIsTextPlainType_thenFalse() {
    var result = TypeUtils.isTextPlainType(Object.class);
    assertFalse(result);
  }

  @ParameterizedTest
  @MethodSource("parseTypeArguments")
  void givenParseType_whenParseType_thenCorrectlyParsed(Class<?> targetType, String paramValue, Object expected) {
    var result = TypeUtils.parseType(targetType, paramValue);
    assertEquals(expected, result);
  }

  static Stream<Arguments> parseTypeArguments() {
    return Stream.of(
      arguments(String.class, "value", "value"),
      arguments(boolean.class, "true", true),
      arguments(Boolean.class, "false", false),
      arguments(short.class, "123", (short) 123),
      arguments(Short.class, "456", (short) 456),
      arguments(int.class, "789", 789),
      arguments(Integer.class, "101", 101),
      arguments(long.class, "999", 999L),
      arguments(Long.class, "1000", 1000L),
      arguments(float.class, "3.14", 3.14f),
      arguments(Float.class, "2.71", 2.71f),
      arguments(double.class, "1.618", 1.618),
      arguments(Double.class, "0.5", 0.5)
    );
  }

}