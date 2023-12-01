package org.bobocode.hoverla.bring.context;

import java.util.Collection;
import java.util.Set;

import org.bobocode.hoverla.bring.bean.BeanDefinition;

/**
 * {@code BeanDefinitionRegistry} is an interface that defines methods for registering,
 * retrieving, and managing bean definitions within Hoverla Bring. It provides the ability
 * to register bean definitions, retrieve them by name or class, and obtain a collection
 * of all registered bean definitions.
 *
 * <p>This interface is typically implemented by classes that serve as registries for
 * managing the definitions of beans in a Hoverla Bring application context.
 *
 * <p>Usage example:
 * <pre>{@code
 * // Create an instance of a class implementing BeanDefinitionRegistry
 * BeanDefinitionRegistry registry = new YourBeanDefinitionRegistryImplementation();
 *
 * // Register a new bean definition
 * BeanDefinition beanDefinition = new BeanDefinition("yourBean", YourClass.class, BeanScope.SINGLETON);
 * registry.registerBeanDefinition("yourBean", beanDefinition);
 *
 * // Retrieve a bean definition by its name
 * BeanDefinition retrievedDefinitionByName = registry.getBeanDefinitionByBeanName("yourBean");
 *
 * // Retrieve a bean definition by its class
 * BeanDefinition retrievedDefinitionByClass = registry.getBeanDefinitionByBeanClass(YourClass.class);
 *
 * // Get all bean definition names
 * Set<String> allBeanDefinitionNames = registry.getAllBeanDefinitionNames();
 *
 * // Get all bean definitions
 * Collection<BeanDefinition> allBeanDefinitions = registry.getAllBeanDefinitions();
 * }</pre>
 *
 * @see BeanDefinition
 */
public interface BeanDefinitionRegistry {

  /**
   * Registers a new bean definition with the specified name and definition.
   *
   * @param beanName       The name to associate with the bean definition.
   * @param beanDefinition The definition of the bean to be registered.
   */
  void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

  /**
   * Retrieves a bean definition by its unique name.
   *
   * @param beanName The name of the bean definition to retrieve.
   * @return The bean definition associated with the specified name.
   */
  BeanDefinition getBeanDefinitionByBeanName(String beanName);

  /**
   * Retrieves a bean definition by its class type.
   *
   * @param beanClass The class type of the bean definition to retrieve.
   * @return The bean definition associated with the specified class type.
   */
  BeanDefinition getBeanDefinitionByBeanClass(Class<?> beanClass);

  /**
   * Retrieves a set containing the names of all registered bean definitions.
   *
   * @return A set of all bean definition names.
   */
  Set<String> getAllBeanDefinitionNames();

  /**
   * Retrieves a collection containing all registered bean definitions.
   *
   * @return A collection of all registered bean definitions.
   */
  Collection<BeanDefinition> getAllBeanDefinitions();
}