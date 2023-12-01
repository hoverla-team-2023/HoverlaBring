package org.bobocode.hoverla.bring.objects;

import org.bobocode.hoverla.bring.factory.BeanFactory;
import org.bobocode.hoverla.bring.processors.BeanFactoryPostProcessor;
import org.bobocode.hoverla.bring.utils.LogRegistry;

public class TestCustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

  @Override
  public void postProcessBeanFactory(BeanFactory beanFactory) {
    LogRegistry.addExecutionLog(TestCustomBeanFactoryPostProcessor.class.getName() + " processed");
  }

}
