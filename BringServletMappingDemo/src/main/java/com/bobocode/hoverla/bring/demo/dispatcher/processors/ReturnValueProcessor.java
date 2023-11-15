package com.bobocode.hoverla.bring.demo.dispatcher.processors;

import java.io.IOException;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ReturnValueProcessor {

  boolean supports(Class<?> type);

  boolean processReturnValue(Object returnValue, Method method, HttpServletRequest request, HttpServletResponse response) throws IOException;

}