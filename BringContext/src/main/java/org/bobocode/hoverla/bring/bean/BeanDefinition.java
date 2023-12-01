package org.bobocode.hoverla.bring.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * {@code BeanDefinition} represents the metadata for a bean in the Hoverla Bring app
 * dependency injection container. It encapsulates information such as the bean name,
 * target class, and the bean's scope.
 **/
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BeanDefinition {

  /**
   * The name of the bean. It serves as a unique identifier within the application context.
   */
  private String beanName;

  /**
   * The target class of the bean, representing the type of object instantiated by the container.
   */
  private Class<?> targetClass;

  /**
   * The scope of the bean, indicating whether the bean is a singleton or prototype.
   *
   * @see BeanScope
   */
  private BeanScope scope;
}