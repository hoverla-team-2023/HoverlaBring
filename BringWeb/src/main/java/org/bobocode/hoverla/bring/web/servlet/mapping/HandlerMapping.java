package org.bobocode.hoverla.bring.web.servlet.mapping;

import jakarta.servlet.http.HttpServletRequest;

import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

public interface HandlerMapping {

  HandlerMethod getHandlerMethod(HttpServletRequest request);

}
