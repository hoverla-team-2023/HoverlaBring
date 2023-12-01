package org.bobocode.hoverla.bring.processors;

/**
 * This interface can be used to different goals to processing bean after instance creation, like dependency injection(some objects or variable from files)
 * After creation of custom implementation this class you need to add it to list of BeanPostProcessors into BeanFactory using addBeanPostProcessor method.
 * this method will be called for every bean after it's instance creation in order that is present in BeanFactoryImpl.beanFactoryPostProcessors
*/

public interface BeanPostProcessor {

  Object postProcessBean(Object bean, String beanName);

}
