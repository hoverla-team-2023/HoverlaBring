package org.bobocode.hoverla.bring.bean;

import lombok.Data;

@Data
public class BeanDefinition {

  private String beanName;
  private Class<?> targetClass;

}
