package org.bobocode.hoverla.bring.web.initializers;

import jakarta.servlet.ServletContext;

import org.bobocode.hoverla.bring.web.servlet.DispatcherServlet;

public abstract class DispatcherServletInitializer implements ServletInitializer {

  @Override
  public void onStartup(ServletContext servletContext) {
    var servlet = new DispatcherServlet(servletContext);
    var registration = servletContext.addServlet("dispatcher", servlet);
    registration.setLoadOnStartup(1);
    registration.setAsyncSupported(true);
    registration.addMapping("/");
  }

}
