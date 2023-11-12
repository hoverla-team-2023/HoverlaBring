package org.bobocode.hoverla.bring.context;

import org.bobocode.hoverla.bring.factory.BeanFactory;

public class HoverlaApplicationContext implements ApplicationContext {

  private BeanDefinitionScanner beanDefinitionScanner;
  private BeanFactory beanFactory;
  private BeanDefinitionRegistry beanDefinitionRegistry;

  @Override
  public ApplicationContext createApplicationContext(String path) {
    return null;
  }

  @Override
  public BeanFactory getBeanFactory() {
    return beanFactory;
  }
}
