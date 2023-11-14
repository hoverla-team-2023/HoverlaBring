package com.bobocode.hoverla.bring.demo.dispatcher;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.Data;

@Data
public class Handler {
    private final Class<?> controllerClass;
    private final Method method;
    private final String path;
    //any other additional data

    public Handler(Class<?> controllerClass, Method method, String path) {
        this.controllerClass = controllerClass;
        this.method = method;
        this.path = path;
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            method.invoke(controllerInstance, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
