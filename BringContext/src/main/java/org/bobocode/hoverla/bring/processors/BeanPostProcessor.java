package org.bobocode.hoverla.bring.processors;

/**
 * The BeanPostProcessor interface provides a hook for customizing bean instances after
 * their creation, allowing for tasks such as dependency injection or other custom
 * processing.
 *
 * <p>Implementations of this interface can be added to the bean factory using the
 * {@code postProcessBean} method will be
 * called for each bean after its instance creation.
 */
public interface BeanPostProcessor {

  /**
   * Post-process the given bean instance before it is registered into the application context.
   *
   * @param bean the recently created bean instance
   * @param beanName the name of the bean
   * @return the updated object that will be registered into the application context with the given "beanName"
   */
  Object postProcessBean(Object bean, String beanName);

}
