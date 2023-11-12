package org.bobocode.hoverla.bring.processors;

import org.bobocode.hoverla.bring.factory.BeanFactory;

public interface BeanFactoryPostProcessor {

  void postProcessBeanFactory(BeanFactory beanFactory);

}
