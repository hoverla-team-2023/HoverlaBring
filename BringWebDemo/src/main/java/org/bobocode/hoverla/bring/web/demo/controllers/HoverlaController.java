package org.bobocode.hoverla.bring.web.demo.controllers;

import org.bobocode.hoverla.bring.web.annotations.Controller;
import org.bobocode.hoverla.bring.web.annotations.RequestMapping;
import org.bobocode.hoverla.bring.web.annotations.RequestMethod;

@Controller
public class HoverlaController {

  @RequestMapping(path = "/hoverla", method = RequestMethod.GET)
  public String hello() {
    return "Hoverla";
  }

}
