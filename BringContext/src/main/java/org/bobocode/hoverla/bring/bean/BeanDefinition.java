package org.bobocode.hoverla.bring.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class BeanDefinition {

  private String beanName;

  private Class<?> targetClass;

  private BeanScope scope;

}
