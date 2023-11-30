package org.bobocode.hoverla.demo.controllers;

import org.bobocode.hoverla.bring.web.annotations.Controller;
import org.bobocode.hoverla.bring.web.annotations.RequestMapping;
import org.bobocode.hoverla.bring.web.annotations.RequestMethod;
import org.bobocode.hoverla.demo.HoverlaException;

@Controller
public class HoverlaController {

  @RequestMapping(path = "/hoverla", method = RequestMethod.GET)
  public String hello() {
    throw new HoverlaException("message");
  }

}
