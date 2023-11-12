package org.bobocode.hoverla.bring.factory;

import org.bobocode.hoverla.bring.processors.BeanFactoryPostProcessor;
import org.bobocode.hoverla.bring.processors.BeanPostProcessor;

import java.util.List;
import java.util.Map;

public class BeanFactory {

  private List<BeanFactoryPostProcessor> beanFactoryPostProcessors;
  private List<BeanPostProcessor> beanPostProcessors;
  private Map<String, Object> beans;

  public Object createBean(String beanName) {
    return null;
  }

  public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {

  }

  public void addBeanPostProcessor(BeanPostProcessor postProcessor) {

  }
}
