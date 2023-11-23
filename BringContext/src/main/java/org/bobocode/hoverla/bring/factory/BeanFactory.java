package org.bobocode.hoverla.bring.factory;

import java.util.Collection;
import java.util.List;

import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.context.BeanDefinitionRegistry;
import org.bobocode.hoverla.bring.processors.BeanFactoryPostProcessor;
import org.bobocode.hoverla.bring.processors.BeanPostProcessor;

public interface BeanFactory {

  void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry);
  Object getBean(String beanName);

  Object tryToInitializeSingletonBean(String beanName);

  void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

  void addBeanPostProcessor(BeanPostProcessor postProcessor);

  BeanDefinition getBeanDefinitionByBeanName(String beanName);

  Collection<BeanDefinition> getRegisteredBeanDefinitions();

}
