package org.bobocode.hoverla.bring.context;

import java.util.Collection;
import java.util.Set;

import org.bobocode.hoverla.bring.bean.BeanDefinition;

/**
 * Interface for implementing BeanDefinition storage, and describe main actions like get, put with it
 */
public interface BeanDefinitionRegistry {

  void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

  BeanDefinition getBeanDefinition(String beanName);

  Set<String> getAllBeanDefinitionNames();

  Collection<BeanDefinition> getAllBeanDefinitions();

}
