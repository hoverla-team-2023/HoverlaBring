package org.bobocode.hoverla.bring.context;

import org.bobocode.hoverla.bring.bean.BeanDefinition;

import java.util.List;

public interface BeanDefinitionScanner {

  List<BeanDefinition> loadBeanDefinitions(String path);

  void registerBeanDefinitions(BeanDefinitionRegistry registry);

}
