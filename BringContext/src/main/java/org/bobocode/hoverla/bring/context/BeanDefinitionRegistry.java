package org.bobocode.hoverla.bring.context;

import java.util.Collection;
import java.util.Set;

import org.bobocode.hoverla.bring.bean.BeanDefinition;

/**
 * The BeanDefinitionRegistry interface defines methods for storing and retrieving BeanDefinitions.
 *
 * @see BeanDefinition
 */
public interface BeanDefinitionRegistry {

  /**
   * Registers a new bean definition with the specified name.
   *
   * @param beanName the name of the bean definition
   * @param beanDefinition the bean definition to be registered
   */
  void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

  /**
   * Retrieves the bean definition associated with the specified bean name.
   *
   * @param beanName the name of the bean definition to retrieve
   * @return the bean definition associated with the given name, or {@code null} if no such definition exists
   */
  BeanDefinition getBeanDefinition(String beanName);

  /**
   * Retrieves a set of all registered bean definition names.
   *
   * @return a set of bean definition names registered in the bean registry
   */
  Set<String> getAllBeanDefinitionNames();

  /**
   * Retrieves a collection of all registered bean definitions.
   *
   * @return a collection of bean definitions registered in the bean registry
   */
  Collection<BeanDefinition> getAllBeanDefinitions();

}
