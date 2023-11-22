package org.bobocode.hoverla.bring.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
public class BeanDefinition {

  private String beanName;

  private Class<?> targetClass;

  private BeanScope scope;

}
