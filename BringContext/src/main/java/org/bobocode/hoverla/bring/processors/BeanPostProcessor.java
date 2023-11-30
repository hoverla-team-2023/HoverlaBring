package org.bobocode.hoverla.bring.processors;

public interface BeanPostProcessor {

  /**
   * This interface can ve used to different goals to processing bean after instance creation, like dependency injection(some objects or variable from files)
   * After creation of custom implementation this class you need to add it to list of BeanPostProcessors into BeanFactory using addBeanPostProcessor method.
   * this method will be called for every bean after it's instance creation in order that is present in BeanFactoryImpl.beanFactoryPostProcessors
   * @param bean recently created bean instance registered into context
   * @param beanName name of bean
   * @return updated object that will be registered into ApplicationContext with given "beanName"
   */
  Object postProcessBean(Object bean, String beanName);

}
