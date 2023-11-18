package org.bobocode.hoverla.bring.context;

import org.bobocode.hoverla.bring.factory.BeanFactory;

public interface ApplicationContext {

  ApplicationContext createApplicationContext(String path);

  BeanFactory getBeanFactory();

}