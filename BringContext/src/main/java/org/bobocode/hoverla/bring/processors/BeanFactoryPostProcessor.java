package org.bobocode.hoverla.bring.processors;

import org.bobocode.hoverla.bring.factory.BeanFactory;

/**
 * A BeanFactoryPostProcessor is a hook for customizing the configuration of the bean factory
 * before any beans are instantiated. Implementations can perform tasks such as modifying
 * bean definitions or adding custom property sources.
 */
public interface BeanFactoryPostProcessor {

  /**
   * Perform custom modification of the bean factory. This method is called before any beans
   * are instantiated, allowing for adjustments to the bean factory configuration.
   *
   * @param beanFactory the bean factory to be post-processed
   */
  void postProcessBeanFactory(BeanFactory beanFactory);
}
