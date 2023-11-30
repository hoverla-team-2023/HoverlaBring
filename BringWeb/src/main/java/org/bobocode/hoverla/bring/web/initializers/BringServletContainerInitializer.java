package org.bobocode.hoverla.bring.web.initializers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Set;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;

import org.bobocode.hoverla.bring.web.exceptions.NoServletInitializerPresentException;
import org.bobocode.hoverla.bring.web.util.BannerUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Basic {@link ServletContainerInitializer} implementation. It is invoked by a servlet container when the web application is started.
 * The implementation receives all classes of type specified by the {@link HandlesTypes} annotation, which is a standard part of the Servlet API
 * used to indicate classes of interest to the container. The initializer then delegates to the {@link ServletInitializer#onStartup(ServletContext)}
 * method of each discovered class.
 * <p>
 * Make sure that each {@link ServletInitializer} implementation has a public default constructor.
 * </p>
 *
 * @see ServletInitializer
 * @see AbstractDispatcherServletInitializer
 */
@Slf4j
@HandlesTypes(ServletInitializer.class)
public class BringServletContainerInitializer implements ServletContainerInitializer {

  /**
   * Handle the application startup. It creates a new instance of the classes specified by the {@link HandlesTypes} annotation and
   * delegates to its {@link ServletInitializer#onStartup(ServletContext)} method.
   *
   * @param classes        the Set of application classes that extend, implement {@link ServletInitializer}
   * @param servletContext the <tt>ServletContext</tt> of the web application that is being started
   */
  @Override
  public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
    log.trace("Initializing servlet container");
    BannerUtils.printBanner("/banner_hoverla.txt");
    var initializers = classes.stream()
      .filter(this::isServletInitializer)
      .toList();

    if (initializers.isEmpty()) {
      var message = "No ServletInitializer found in the classpath. Consider extending the AbstractDispatcherServletInitializer to create a dispatcher servlet.";

      log.error(message);
      throw new NoServletInitializerPresentException(message);
    }

    for (Class<?> initializer : initializers) {
      var servletInitializer = (ServletInitializer) getInstance(initializer);
      servletInitializer.onStartup(servletContext);
    }
  }

  private boolean isServletInitializer(Class<?> clazz) {
    return !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()) &&
           ServletInitializer.class.isAssignableFrom(clazz);
  }

  private Object getInstance(Class<?> clazz) throws ServletException {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      var message = "Unable to create instance of class %s. Please make sure that the class has a public default constructor".formatted(clazz.getName());

      log.error(message, e);
      throw new ServletException(message, e);
    }
  }

}
