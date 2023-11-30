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

}


