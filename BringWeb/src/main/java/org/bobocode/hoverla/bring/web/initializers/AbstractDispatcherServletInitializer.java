package org.bobocode.hoverla.bring.web.initializers;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.bobocode.hoverla.bring.context.HoverlaApplicationContext;
import org.bobocode.hoverla.bring.web.servlet.DispatcherServlet;

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
    var servlet = new DispatcherServlet(servletContext, new HoverlaApplicationContext(getPackagesToScan()));
    var registration = servletContext.addServlet("dispatcher", servlet);
    registration.setLoadOnStartup(1);
    registration.setAsyncSupported(true);
    registration.addMapping("/");

    log.info("DispatcherServlet initialized");
  }

  /**
   * Get the base packages to be scanned for component classes.
   *
   * @return the base packages to be scanned.
   */
  protected abstract String getPackagesToScan();


}
