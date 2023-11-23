package org.bobocode.hoverla.bring.context;

import java.util.Set;

import org.bobocode.hoverla.bring.bean.BeanDefinition;

public interface BeanDefinitionRegistry {

  void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

  BeanDefinition getBeanDefinition(String beanName);

  Set<String> getAllBeanDefinitionNames();

}
