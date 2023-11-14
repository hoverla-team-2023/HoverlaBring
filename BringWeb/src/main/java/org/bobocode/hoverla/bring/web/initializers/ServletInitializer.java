package org.bobocode.hoverla.bring.web.initializers;

import jakarta.servlet.ServletContext;

public interface ServletInitializer {

  void onStartup(ServletContext servletContext);

}
