package org.bobocode.hoverla.bring.web.demo.controllers;

import org.bobocode.hoverla.bring.web.annotations.Controller;
import org.bobocode.hoverla.bring.web.annotations.PathVariable;
import org.bobocode.hoverla.bring.web.annotations.RequestMapping;
import org.bobocode.hoverla.bring.web.annotations.RequestMethod;

@Controller
public class HoverlaController {

  @RequestMapping(path = "/hoverla/{name}/comment/{hello}", method = RequestMethod.GET)
  public String hello(@PathVariable("name") String name, @PathVariable("hello") String hello) {
    return "Hoverla";
  }

}
