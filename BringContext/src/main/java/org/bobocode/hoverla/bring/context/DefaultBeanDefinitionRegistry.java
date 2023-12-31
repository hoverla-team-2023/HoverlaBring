package org.bobocode.hoverla.bring.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bobocode.hoverla.bring.bean.BeanDefinition;

import lombok.extern.slf4j.Slf4j;

/**
 * Default class in context module responsible for BeanDefinition storing
 */
@Slf4j
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
  public BeanDefinition getBeanDefinitionByBeanName(String beanName) {
    if (beanName == null) {
      throw new IllegalArgumentException("Bean name should not be null");
    }
    return beanDefinitions.get(beanName);
  }

  @Override
  public BeanDefinition getBeanDefinitionByBeanClass(Class<?> beanClass) {
    List<BeanDefinition> beanDefinitionsByClass = beanDefinitions.values()
      .stream()
      .filter(bd -> beanClass.isAssignableFrom(bd.getTargetClass()))
      .collect(Collectors.toList());
    if (beanDefinitionsByClass.isEmpty()) {
      throw new IllegalArgumentException("No beans found by class " + beanClass.getName());
    }
    if (beanDefinitionsByClass.size() > 1) {
      String msg = String.join(",", beanDefinitionsByClass.stream().map(bd -> bd.getTargetClass().getName()).collect(Collectors.toList()));
      throw new IllegalArgumentException("More than 1 bean found for class " + beanClass.getName() + " defined bean names" + msg);
    }
    log.debug("Found next BeanDefinition: {} for class {}", beanDefinitionsByClass.get(0), beanClass.getName());
    return beanDefinitionsByClass.get(0);
  }

  /**
   * @return all registered BeanDefinition names
   */
  @Override
  public Set<String> getAllBeanDefinitionNames() {
    return beanDefinitions.keySet();
  }

  /**
   * this method will return all registered BeanDefinitions
   */
  @Override
  public Collection<BeanDefinition> getAllBeanDefinitions() {
    return beanDefinitions.values();
  }

}
