package org.bobocode.hoverla.bring.context;

import java.util.List;

import org.bobocode.hoverla.bring.bean.BeanDefinition;

public interface BeanDefinitionScanner {

  List<BeanDefinition> loadBeanDefinitions(String path);

}
