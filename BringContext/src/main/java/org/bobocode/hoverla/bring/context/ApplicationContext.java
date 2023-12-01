package org.bobocode.hoverla.bring.context;

import org.bobocode.hoverla.bring.factory.BeanFactory;

/**
 * {@code ApplicationContext} is an interface that represents the application context
 * in Hoverla Bring. It defines
 * methods for obtaining beans and accessing the underlying {@link BeanFactory}.
 *
 * <p>The application context serves as a central hub for managing beans and their
 * dependencies within the Hoverla Bring container.
 *
 * <p>Usage example:
 * <pre>{@code
 * // Obtain the application context
 * ApplicationContext context = new HoverlaApplicationContext("com.your.package");

 * // Retrieve the underlying BeanFactory
 * BeanFactory beanFactory = context.getBeanFactory();
 *
 * // Get a bean by its name
 * Object beanByName = context.getBean("yourBeanName");
 *
 * // Get a bean by its class
 * Object beanByClass = context.getBean(YourClass.class);
 * }</pre>
 *
 * @see HoverlaApplicationContext
 * @see BeanFactory
 */
public interface ApplicationContext {

  /**
   * Retrieves the underlying {@link BeanFactory} associated with this application context.
   *
   * @return The BeanFactory instance used by the application context.
   */
  BeanFactory getBeanFactory();

  /**
   * Retrieves a bean from the application context by its unique name.
   *
   * @param beanName The name of the bean to retrieve.
   * @return The bean instance associated with the specified name.
   */
  Object getBean(String beanName);

  /**
   * Retrieves a bean from the application context by its class.
   *
   * @param beanClass The class type of the bean to retrieve.
   * @return The bean instance associated with the specified class type.
   */
  Object getBean(Class<?> beanClass);
}
