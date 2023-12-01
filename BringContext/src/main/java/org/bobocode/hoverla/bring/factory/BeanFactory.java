package org.bobocode.hoverla.bring.factory;

import java.util.Collection;
import java.util.Set;

import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.context.BeanDefinitionRegistry;
import org.bobocode.hoverla.bring.processors.BeanFactoryPostProcessor;
import org.bobocode.hoverla.bring.processors.BeanPostProcessor;

/**
 * The {@code BeanFactory} interface represents a central registry for managing beans in a bean-oriented application context.
 * It defines methods for accessing and managing bean definitions, initializing beans, and adding various processors.
 * Implementations of this interface are responsible for creating and managing beans based on provided bean definitions.
 * <p>
 * A bean factory serves as the core component in a bean container, allowing the retrieval of beans by name and handling
 * the lifecycle of beans, including their instantiation, initialization, and destruction.
 * </p>
 * <p>
 * The interface provides methods for adding {@link BeanFactoryPostProcessor} and {@link BeanPostProcessor} instances,
 * which allow customization and manipulation of bean definitions and instances during the bean factory's initialization
 * and runtime phases.
 * </p>
 *
 * @see BeanDefinitionRegistry
 * @see BeanFactoryPostProcessor
 * @see BeanPostProcessor
 * @see BeanDefinition
 */
public interface BeanFactory {

  /**
   * Sets the {@link BeanDefinitionRegistry} for this bean factory. The registry is responsible for managing bean
   * definitions within the factory.
   *
   * @param beanDefinitionRegistry the bean definition registry to be set
   */
  void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry);
  /**
   * Retrieves the bean instance with the specified name from the bean factory.
   *
   * @param beanName the name of the bean to retrieve
   *
   * @return the bean instance associated with the given name
   */
  Object getBean(String beanName);

  /**
   * Retrieves the bean instance by class of bean ar classes that extends/implements class given in @param beanClass
   *
   * @param beanClass the class of the bean to retrieve
   *
   * @return the bean instance associated with the given name
   * Please note if you want to get bean by interface that has 2 or more implementation marked with @Component
   * you will get exception
   */
  Object getBean(Class<?> beanClass);
  /**
   * Attempts to initialize a singleton bean with the specified name. This method is intended to be used for eagerly
   * initializing singleton beans during application startup.
   *
   * @param beanName the name of the singleton bean to initialize
   *
   * @return the initialized singleton bean instance, or {@code null} if the bean is not a singleton or cannot be
   * initialized
   */

  Object tryToInitializeSingletonBean(String beanName);

  /**
   * this method return all existed BeanFactoryPostProcessors
   */
  Set<BeanFactoryPostProcessor> getBeanFactoryPostProcessors();

  /**
   * Adds a {@link BeanFactoryPostProcessor} to the bean factory. Post-processors are invoked during the bean
   * factory's initialization phase, allowing for customization of bean definitions before the actual bean
   * instantiation occurs.
   *
   * @param postProcessor the bean factory post-processor to add
   */

  void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);
  /**
   * Adds a {@link BeanPostProcessor} to the bean factory. Post-processors are invoked during the lifecycle of
   * individual beans, allowing for custom behavior before and after bean initialization.
   *
   * @param postProcessor the bean post-processor to add
   */

  void addBeanPostProcessor(BeanPostProcessor postProcessor);
  /**
   * Retrieves the {@link BeanDefinition} associated with the specified bean name.
   *
   * @param beanName the name of the bean whose definition is to be retrieved
   *
   * @return the bean definition associated with the given name, or {@code null} if no such definition exists
   */

  BeanDefinition getBeanDefinitionByBeanName(String beanName);
  /**
   * Retrieves a collection of all registered {@link BeanDefinition}s within the bean factory.
   *
   * @return a collection of bean definitions registered in the bean factory
   */

  Collection<BeanDefinition> getRegisteredBeanDefinitions();

  /**
   * get list of created beans existed in BeanFactory
   */
  Collection<Object> getAllBeans();

}
