package org.bobocode.hoverla.bring.context;

import org.bobocode.hoverla.bring.bean.BeanDefinition;

public interface BeanDefinitionRegistry {

  void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

  BeanDefinition getBeanDefinition(String beanName);
}
