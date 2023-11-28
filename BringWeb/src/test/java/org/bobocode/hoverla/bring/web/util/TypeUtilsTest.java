package org.bobocode.hoverla.bring.web.util;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

}