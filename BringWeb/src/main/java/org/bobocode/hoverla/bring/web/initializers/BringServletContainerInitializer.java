package org.bobocode.hoverla.bring.web.initializers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Set;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;

// TODO: 14.11.2023 add javaDoc
@HandlesTypes(ServletInitializer.class)
public class BringServletContainerInitializer implements ServletContainerInitializer {

  @Override
  public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
    c.stream()
      .filter(this::isServletInitializer)
      .map(this::getInstance)
      .map(ServletInitializer.class::cast)
      .forEach(servletInitializer -> servletInitializer.onStartup(servletContext));
  }

  private boolean isServletInitializer(Class<?> clazz) {
    return !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()) &&
           ServletInitializer.class.isAssignableFrom(clazz);
  }

  // TODO: 14.11.2023 add descriptive logging
  private Object getInstance(Class<?> clazz) {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

}
