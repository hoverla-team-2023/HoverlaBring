package org.bobocode.hoverla.bring.web.util;

import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeUtils {

  private static final Map<Class<?>, Class<?>> WRAPPER_TYPES = Map.of(
    Integer.class, int.class,
    Byte.class, byte.class,
    Character.class, char.class,
    Boolean.class, boolean.class,
    Double.class, double.class,
    Float.class, float.class,
    Long.class, long.class,
    Short.class, short.class,
    Void.class, void.class
  );

  public static boolean isTextPlainType(Class<?> type) {
    return isWrapperType(type) || String.class.isAssignableFrom(type);
  }

  public static boolean isWrapperType(Class<?> type) {
    return WRAPPER_TYPES.containsKey(type);
  }

}


