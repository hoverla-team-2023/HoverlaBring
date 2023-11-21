package org.bobocode.hoverla.bring.context;

import org.bobocode.hoverla.bring.factory.BeanFactory;

/**
 * The ApplicationContext interface represents the core interface for an application context,
 * providing access to the underlying BeanFactory.
 *
 * @see BeanFactory
 * @since 1.0
 */
public interface ApplicationContext {

  /**
   * Retrieve the underlying BeanFactory for this application context.
   *
   * @return the BeanFactory used by this application context
   */
  BeanFactory getBeanFactory();

}
