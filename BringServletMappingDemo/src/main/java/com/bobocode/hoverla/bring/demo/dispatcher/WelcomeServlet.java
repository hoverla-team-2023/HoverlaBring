package com.bobocode.hoverla.bring.demo.dispatcher;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/")
public class WelcomeServlet extends HttpServlet {

    private SimpleHandlerMapping handlerMapping;

    @Override
    public void init() {
        handlerMapping = new SimpleHandlerMapping();
        handlerMapping.scanControllers("com.bobocode.hoverla.bring.demo.controller");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        Handler handler = handlerMapping.getHandler(requestURI);

        if (handler != null) {
            handler.handleRequest(request, response);
        } else {
            response.getWriter().write("No handler found for the request URL: " + requestURI);
        }
    }
}


