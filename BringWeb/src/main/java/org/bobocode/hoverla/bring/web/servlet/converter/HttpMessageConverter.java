package org.bobocode.hoverla.bring.web.servlet.converter;

import java.io.IOException;
import java.lang.reflect.Type;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This interface is used to convert and write the return value of a {@link org.bobocode.hoverla.bring.web.annotations.Controller} to a {@link HttpServletResponse}.
 */
public interface HttpMessageConverter {

  boolean canWrite(Class<?> type, String contentType);

  void write(Object value, HttpServletResponse response, String contentType) throws IOException;

  boolean canRead(Type type, String contentType);

  Object read(Type type, HttpServletRequest request, String contentType) throws IOException;

  boolean isSupportedContentType(String contentType);

}
