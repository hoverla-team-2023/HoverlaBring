package org.bobocode.hoverla.bring.web.servlet.processor;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;

public interface ReturnValueProcessor {

  boolean supports(Class<?> type);

  boolean processReturnValue(Object returnValue, HandlerMethod method, HttpServletRequest request, HttpServletResponse response) throws IOException;

}