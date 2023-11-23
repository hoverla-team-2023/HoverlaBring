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
      arguments(char.class),
      arguments(Void.class)
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

  static Stream<Arguments> primitiveTypes() {
    return Stream.of(
      arguments(byte.class),
      arguments(short.class),
      arguments(int.class),
      arguments(long.class),
      arguments(float.class),
      arguments(double.class),
      arguments(boolean.class),
      arguments(char.class)
    );
  }

  @ParameterizedTest
  @MethodSource("primitiveTypes")
  void givenIsPrimitive_whenIsPrimitiveOrWrapper_thenTrue(Class<?> type) {
    var result = TypeUtils.isPrimitiveOrWrapper(type);
    assertTrue(result);
  }

  static Stream<Arguments> wrapperTypes() {
    return Stream.of(
      arguments(Byte.class),
      arguments(Short.class),
      arguments(Integer.class),
      arguments(Long.class),
      arguments(Float.class),
      arguments(Double.class),
      arguments(Boolean.class),
      arguments(Character.class)
    );
  }

  @ParameterizedTest
  @MethodSource("wrapperTypes")
  void givenIsWrapper_whenIsPrimitiveOrWrapper_thenTrue(Class<?> type) {
    var result = TypeUtils.isPrimitiveOrWrapper(type);
    assertTrue(result);
  }

  @Test
  void givenObject_whenIsPrimitiveOrWrapper_thenFalse() {
    var result = TypeUtils.isPrimitiveOrWrapper(Object.class);
    assertFalse(result);
  }

}