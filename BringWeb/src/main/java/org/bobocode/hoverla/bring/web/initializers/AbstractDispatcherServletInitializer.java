package org.bobocode.hoverla.bring.web.initializers;

import jakarta.servlet.ServletContext;

import org.bobocode.hoverla.bring.web.servlet.DispatcherServlet;

import lombok.extern.slf4j.Slf4j;

/**
 * Basic {@link ServletInitializer} implementation. This initializer allows developers to easily register a {@link DispatcherServlet} in the servlet container
 * by extending this class. Please note that your implementation should have a public default constructor.
 */
@Slf4j
public abstract class AbstractDispatcherServletInitializer implements ServletInitializer {

  /**
   * Create a new instance of the {@link DispatcherServlet} class and register it in the {@link ServletContext context}.
   *
   * @param servletContext the <tt>ServletContext</tt> of the web application that is being started
   */
  @Override
  public void onStartup(ServletContext servletContext) {
    var servlet = new DispatcherServlet(servletContext);
    var registration = servletContext.addServlet("dispatcher", servlet);
    registration.setLoadOnStartup(1);
    registration.setAsyncSupported(true);
    registration.addMapping(getServletMapping());

    log.info("DispatcherServlet initialized");
  }

  /**
   * Provide the mapping URL for the dispatcher servlet.
   *
   * @return mapping URL
   */
  protected abstract String getServletMapping();

}
