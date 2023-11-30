package org.bobocode.hoverla.bring.web.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.bobocode.hoverla.bring.web.servlet.entity.ResponseEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeUtils {

  public static boolean isTextPlainType(Type type) {
    if (type instanceof Class<?> clazz) {
      return isPrimitiveOrWrapper(clazz) || String.class.isAssignableFrom(clazz);
    }
    return false;
  }

  public static boolean isResponseEntity(Type type) {
    if (type instanceof ParameterizedType parameterizedType) {
      return ResponseEntity.class.isAssignableFrom((Class<?>) parameterizedType.getRawType());
    }
    return ResponseEntity.class == type; // for raw type ResponseEntity without generic type
  }

  public static Object parseType(Class<?> targetType, String paramValue) {
    if (targetType == String.class) {
      return paramValue;
    } else if (targetType == boolean.class || targetType == Boolean.class) {
      return Boolean.parseBoolean(paramValue);
    } else if (targetType == short.class || targetType == Short.class) {
      return Short.parseShort(paramValue);
    } else if (targetType == int.class || targetType == Integer.class) {
      return Integer.parseInt(paramValue);
    } else if (targetType == long.class || targetType == Long.class) {
      return Long.parseLong(paramValue);
    } else if (targetType == double.class || targetType == Double.class) {
      return Double.parseDouble(paramValue);
    } else if (targetType == float.class || targetType == Float.class) {
      return Float.parseFloat(paramValue);
    }

    log.warn("Unsupported target type {} for parsing value {}", targetType.getTypeName(), paramValue);
    return null;
  }

}


