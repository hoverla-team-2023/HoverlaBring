package org.bobocode.hoverla.bring.factory;

import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.context.BeanDefinitionRegistry;
import org.bobocode.hoverla.bring.processors.BeanFactoryPostProcessor;
import org.bobocode.hoverla.bring.processors.BeanPostProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanFactoryImpl implements BeanFactory {

  private List<BeanFactoryPostProcessor> beanFactoryPostProcessors;
  private BeanDefinitionRegistry beanDefinitionRegistry;
  private List<BeanPostProcessor> beanPostProcessors;
  private Map<String, Object> beans;

  public void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) {
    this.beanDefinitionRegistry = beanDefinitionRegistry;
  }

  @Override
  public Object getBean(String beanName) {
    return null;
  }

  @Override
  public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
  }

  @Override
  public void addBeanPostProcessor(BeanPostProcessor postProcessor) {
  }

  @Override
  public void addBeanDefinition(String beanDefinitionName, BeanDefinition beanDefinition) {
    this.beanDefinitionRegistry.registerBeanDefinition(beanDefinitionName, beanDefinition);
  }
  @Override
  public BeanDefinition getBeanDefinitionByName(String beanName) {
    return beanDefinitionRegistry.getBeanDefinition(beanName);
  }
}
