package org.bobocode.hoverla.bring.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The BeanDefinition class represents metadata for a bean in the application context.
 *
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BeanDefinition {

  /**
   * The name of the bean.
   */
  private String beanName;

  /**
   * The class type of the bean.
   */
  private Class<?> targetClass;

  /**
   * The scope of the bean.
   */
  private BeanScope scope;

}
