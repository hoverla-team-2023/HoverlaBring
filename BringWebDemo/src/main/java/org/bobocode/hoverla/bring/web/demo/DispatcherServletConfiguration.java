package org.bobocode.hoverla.bring.web.demo;

import org.bobocode.hoverla.bring.web.initializers.AbstractDispatcherServletInitializer;

public class DispatcherServletConfiguration extends AbstractDispatcherServletInitializer {

  @Override
  protected String getServletMapping() {
    return "/";
  }

  @Override
  public String getPackagesToScan() {
    return "org.bobocode.hoverla.bring.web.demo";
  }

}
