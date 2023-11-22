package org.bobocode.hoverla.bring.context;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is registry for {@link BeanDefinition}
 */
import org.bobocode.hoverla.bring.bean.BeanDefinition;

public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {

  private final Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

  /**
   * Registers a new bean definition with the specified name in the registry
   *
   * @param beanName       the name of the bean to register.
   * @param beanDefinition the definition of the bean to register.
   */
  @Override
  public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
    beanDefinitions.put(beanName, beanDefinition);
    if (beanName == null) {
      throw new IllegalArgumentException("Bean name can't be null");
    }
    if (beanDefinition == null) {
      throw new IllegalArgumentException("BeanDefinition can't be null");
    }
    if (beanDefinitions.containsKey(beanName)) {
      throw new IllegalArgumentException("Failed to set, Bean with name " + beanName + "already exists");
    }
    beanDefinitions.put(beanName, beanDefinition);
  }

  /**
   * Retrieves a bean definition by its name from the registry.
   *
   * @param beanName the name of the bean to retrieve.
   *
   * @return the BeanDefinition associated with the given name, or null if no such definition exists.
   */
  @Override
  public BeanDefinition getBeanDefinition(String beanName) {
    if (beanName == null) {
      throw new IllegalArgumentException("Bean name should not be null");
    }
    return beanDefinitions.get(beanName);
  }

}
