package org.bobocode.hoverla.bring.web.initializers;

import jakarta.servlet.ServletContext;

/**
 * Initializes a servlet. It is invoked by {@link org.bobocode.hoverla.bring.web.initializers.BringServletContainerInitializer}
 * on application startup.
 * <p>
 * Consider extending {@link AbstractDispatcherServletInitializer} to easily register a {@link org.bobocode.hoverla.bring.web.servlet.DispatcherServlet}
 * in the servlet container.
 * </p>
 *
 * @see org.bobocode.hoverla.bring.web.initializers.BringServletContainerInitializer
 * @see AbstractDispatcherServletInitializer
 */
public interface ServletInitializer {

  /**
   * Create a new servlet and register it in the servlet context.
   *
   * @param servletContext the <tt>ServletContext</tt> of the web application that is being started
   */
  void onStartup(ServletContext servletContext);

  String getPackagesToScan();
}
