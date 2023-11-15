package com.bobocode.hoverla.bring.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.bobocode.hoverla.bring.demo.annotation.Controller;
import com.bobocode.hoverla.bring.demo.annotation.RequestMapping;
import com.bobocode.hoverla.bring.demo.dispatcher.HttpEntity;

@Controller
public class BobocodeController {

  @RequestMapping("bobocode/hoverla")
  public HttpEntity<List<TeamDTO>> helloWorld(HttpServletRequest request, HttpServletResponse response) {
    Map<String, List<String>> headers = new HashMap<>();
    //  headers.put("Content-Type", List.of("application/json"));

    String responseBody = "Hello, World!";

    return new HttpEntity<>(List.of(new TeamDTO("hoverla")), headers);
  }

  @RequestMapping("bobocode/training")
  public HttpEntity<String> training(HttpServletRequest request, HttpServletResponse response) {
    return new HttpEntity<>("hoverla", new HashMap<>());
  }

  @RequestMapping("bobocode/hoverla-string")
  public String hoverlaString(HttpServletRequest request, HttpServletResponse response) {
    return "hoverla";
  }

}

record TeamDTO(String team) {

}
