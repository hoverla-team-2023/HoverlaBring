package org.bobocode.hoverla.bring.factory;

import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.processors.BeanFactoryPostProcessor;
import org.bobocode.hoverla.bring.processors.BeanPostProcessor;

public interface BeanFactory {

  Object getBean(String beanName);

  void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

  void addBeanPostProcessor(BeanPostProcessor postProcessor);

  void addBeanDefinition(String beanDefinitionName, BeanDefinition beanDefinition);

  BeanDefinition getBeanDefinitionByName(String beanName);

}
