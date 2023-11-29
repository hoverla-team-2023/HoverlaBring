package org.bobocode.hoverla.demo;

import org.bobocode.hoverla.demo.controllers.HoverlaController;
import org.bobocode.hoverla.bring.web.initializers.AbstractDispatcherServletInitializer;

public class DispatcherServletConfiguration extends AbstractDispatcherServletInitializer {

  @Override
  protected String getServletMapping() {
    return "/";
  }

  @Override
  protected Object[] controllers() {
    return new Object[] { new HoverlaController() };
  }

  @Override
  protected Object[] controllerAdvices() {
    return new Object[] { new HoverlaControllerAdvice() };
  }

}
