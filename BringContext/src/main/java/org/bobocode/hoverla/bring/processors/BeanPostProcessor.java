package org.bobocode.hoverla.bring.processors;

public interface BeanPostProcessor {

  void postProcessBeforeInitialization(Object bean, String beanName);
  void postProcessAfterInitialization(Object bean, String beanName);

}
