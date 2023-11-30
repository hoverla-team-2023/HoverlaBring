package org.bobocode.hoverla.bring.factory;

import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.context.BeanDefinitionRegistry;
import org.bobocode.hoverla.bring.processors.BeanFactoryPostProcessor;
import org.bobocode.hoverla.bring.processors.BeanPostProcessor;

import java.util.Collection;

public interface BeanFactory {

  void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry);
  Object getBean(String beanName);

  Object tryToInitializeSingletonBean(String beanName);

  void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

  void addBeanPostProcessor(BeanPostProcessor postProcessor);

  BeanDefinition getBeanDefinitionByBeanName(String beanName);

  Collection<BeanDefinition> getRegisteredBeanDefinitions();

  Collection<Object> getAllBeans();
}
