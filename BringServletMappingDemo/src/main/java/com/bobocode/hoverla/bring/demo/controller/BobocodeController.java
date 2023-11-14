package com.bobocode.hoverla.bring.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.bobocode.hoverla.bring.demo.annotation.Controller;
import com.bobocode.hoverla.bring.demo.annotation.RequestMapping;

@Controller
public class BobocodeController {

    @RequestMapping("bobocode/hoverla")
    public void helloWorld(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("hello");
    }

    @RequestMapping("bobocode/traning")
    public void asdgf(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("hello");
    }
}
