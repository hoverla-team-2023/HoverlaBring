package org.bobocode.hoverla.bring.bean;

/**
 * {@code BeanScope} is an enumeration representing the bean scope in Hoverla Bring,
 * It defines the different scopes that a bean can have.
 *
 * <p>The enumeration consists of the following scope:
 * <ul>
 *   <li>{@code SINGLETON}: Indicates that a single instance of the bean is created
 *   and shared throughout the entire application context.</li>
 * </ul>
 *
 * @see BeanDefinition
 */
public enum BeanScope {
  SINGLETON
}
