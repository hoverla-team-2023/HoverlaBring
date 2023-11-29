package org.bobocode.hoverla.demo;

import org.bobocode.hoverla.bring.web.initializers.AbstractDispatcherServletInitializer;

public class DispatcherServletConfiguration extends AbstractDispatcherServletInitializer {

  @Override
  public String getPackagesToScan() {
    return "org.bobocode.hoverla.demo";
  }
}
