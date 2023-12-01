package org.bobocode.hoverla.bring.objects;

import org.bobocode.hoverla.bring.processors.BeanPostProcessor;
import org.bobocode.hoverla.bring.utils.LogRegistry;

public class TestCustomBeanPostProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessBean(Object bean, String beanName) {
    LogRegistry.addExecutionLog(TestCustomBeanPostProcessor.class.getName() + " worked");
    return bean;
  }

}
