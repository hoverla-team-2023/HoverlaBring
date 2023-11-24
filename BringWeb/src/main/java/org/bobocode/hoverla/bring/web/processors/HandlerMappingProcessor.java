package org.bobocode.hoverla.bring.web.processors;

import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.factory.BeanFactory;
import org.bobocode.hoverla.bring.processors.BeanPostProcessor;
import org.bobocode.hoverla.bring.web.servlet.mapping.AnnotationBasedHandlerMapping;
import org.bobocode.hoverla.bring.web.servlet.mapping.HandlerMapping;

public class HandlerMappingProcessor implements BeanPostProcessor {

  private final BeanFactory beanFactory;

  public HandlerMappingProcessor(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {
    return null;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    BeanDefinition beanDefinitionByBeanName = beanFactory.getBeanDefinitionByBeanName(beanName);
    // todo define it's controller, and save it in some registry
    return null;
  }

}
