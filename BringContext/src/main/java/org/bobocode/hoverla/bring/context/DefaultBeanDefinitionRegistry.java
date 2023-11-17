package org.bobocode.hoverla.bring.context;

import org.bobocode.hoverla.bring.bean.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is registry for {@link BeanDefinition}
 */
public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {

  private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

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

  @Override
  public BeanDefinition getBeanDefinition(String beanName) {
    if (beanName == null) {
      throw new IllegalArgumentException("Bean name should not be null");
    }
    return beanDefinitions.get(beanName);
  }

}
