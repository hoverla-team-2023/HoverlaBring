package com.bobocode.hoverla.bring.demo.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.bobocode.hoverla.bring.demo.annotation.Controller;
import com.bobocode.hoverla.bring.demo.annotation.RequestMapping;

@Controller
public class HelloWorldController {

  @RequestMapping("hello")
  public void helloWorld(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.getWriter().write("Hello: " + request.getRequestURI());

  }

  @RequestMapping("hello/world")
  public void helloWorld2(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.getWriter().write("Hello world!");

  }

  @RequestMapping("hello/hoverla")
  public void helloHoverla(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.getWriter().write("Hello hoverla team!");
  }

}
