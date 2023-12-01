package org.bobocode.hoverla.bring.context;

import java.util.List;

import org.bobocode.hoverla.bring.bean.BeanDefinition;

/**
 * Interface for a bean definition scanner that is responsible for loading
 * bean definitions based on a specified path.
 */
public interface BeanDefinitionScanner {

  /**
   * Load bean definitions from the specified path.
   *
   * @param path The path to scan for bean definitions.
   * @return A list of {@link BeanDefinition}
   */
  List<BeanDefinition> loadBeanDefinitions(String path);

}
