package org.bobocode.hoverla.bring.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.bean.BeanScope;
import org.bobocode.hoverla.bring.context.BeanDefinitionRegistry;
import org.bobocode.hoverla.bring.exception.BeanCreationException;
import org.bobocode.hoverla.bring.processors.BeanFactoryPostProcessor;
import org.bobocode.hoverla.bring.processors.BeanPostProcessor;

/**
 * BeanFactoryImpl class is responsible for creation of beans from bean definitions and dependency injection
 */
public class BeanFactoryImpl implements BeanFactory {

  private Set<BeanFactoryPostProcessor> beanFactoryPostProcessors = new HashSet<>();
  private BeanDefinitionRegistry beanDefinitionRegistry;
  private Set<BeanPostProcessor> beanPostProcessors = new HashSet<>();
  private Map<String, Object> beans = new HashMap<>();

  public void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) {
    this.beanDefinitionRegistry = beanDefinitionRegistry;
  }

  /**
   * this method will get bean if already created, or create it based on existed bean definition and return
   *
   * @param beanName is name of bean that should be the same as bean name
   *
   * @return new or early created bean with injected dependency
   */
  @Override
  public Object getBean(String beanName) {
    BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
    if (beanDefinition == null) {
      throw new BeanCreationException("Bean definition for bean with name: " + beanName + " does not exist");
    }
    if (BeanScope.SINGLETON.equals(beanDefinition.getScope())) {
      return tryToInitializeSingletonBean(beanName);
    }
    throw new BeanCreationException("Scope: " + beanDefinition.getScope() + "is not supported");
  }

  @Override
  public Object tryToInitializeSingletonBean(String beanName) {
    if (beans.containsKey(beanName)) {
      return beans.get(beanName);
    }
    Object createdBean;
    try {
      createdBean = doCreateSingletoneBean(beanName);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new BeanCreationException("Error happened during crete of bean with name: " + beanName, e);
    }
    beans.put(beanName, createdBean);
    beanPostProcessors.forEach(bpp -> bpp.postProcessBeforeInitialization(createdBean, beanName));
    beanPostProcessors.forEach(bpp -> bpp.postProcessAfterInitialization(createdBean, beanName));
    return createdBean;
  }

  private Object doCreateSingletoneBean(String beanName) throws InvocationTargetException, InstantiationException, IllegalAccessException {
    BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
    if (beanDefinition == null) {
      throw new BeanCreationException("Can't find BeanDefinition for name" + beanName);
    }
    Class<?> targetClass = beanDefinition.getTargetClass();
    if (targetClass == null) {
      throw new BeanCreationException("BeamDefinition for bean name" + beanName + "should contain target class");
    }
    Object bean;
    for (Constructor<?> constructor : targetClass.getConstructors()) {
      if (constructor.getParameterCount() == 0) {
        return constructor.newInstance();
      }
    }
    throw new BeanCreationException("Bean with name " + beanName + "does not have constructor without arguments");
  }

  @Override
  public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
    if (postProcessor == null) {
      throw new IllegalArgumentException("BeamFactoryPostProcessor should not be null");
    }
    this.beanFactoryPostProcessors.add(postProcessor);
  }

  @Override
  public void addBeanPostProcessor(BeanPostProcessor postProcessor) {
    if (postProcessor == null) {
      throw new IllegalArgumentException("BeamPostProcessor should not be null");
    }
    beanPostProcessors.add(postProcessor);
  }

  @Override
  public BeanDefinition getBeanDefinitionByName(String beanName) {
    return beanDefinitionRegistry.getBeanDefinition(beanName);
  }

}
