package org.bobocode.hoverla.bring.factory;

import lombok.extern.slf4j.Slf4j;
import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.bean.BeanScope;
import org.bobocode.hoverla.bring.context.BeanDefinitionRegistry;
import org.bobocode.hoverla.bring.exception.BeanCreationException;
import org.bobocode.hoverla.bring.processors.BeanFactoryPostProcessor;
import org.bobocode.hoverla.bring.processors.BeanPostProcessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * BeanFactoryImpl class is responsible for creation of beans from bean definitions and dependency injection
 */
@Slf4j
public class BeanFactoryImpl implements BeanFactory {

  private final Set<BeanFactoryPostProcessor> beanFactoryPostProcessors = new HashSet<>();
  private BeanDefinitionRegistry beanDefinitionRegistry;
  private final Set<BeanPostProcessor> beanPostProcessors = new HashSet<>();
  private final Map<String, Object> beans = new HashMap<>();

  /**
   * This method set BeanDefinitionRegistry into BeanFactory
   */
  @Override
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
    throw new BeanCreationException("Scope: " + beanDefinition.getScope() + " is not supported");
  }

  @Override
  public Collection<Object> getAllBeans() {
    return beans.values();
  }

  /**
   * This method will return bean if bean with selected name already exist or create it if not
   */
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
    log.debug("Started post processing for bean with name {}", beanName);
    for (BeanPostProcessor bpp : beanPostProcessors) {
      createdBean = bpp.postProcessBean(createdBean, beanName);
    }
    log.debug("Post processing for bean with name {} finished", beanName);
    beans.put(beanName, createdBean);
    return createdBean;
  }

  private Object doCreateSingletoneBean(String beanName) throws InvocationTargetException, InstantiationException, IllegalAccessException {
    BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
    if (beanDefinition == null) {
      throw new BeanCreationException("Can't find BeanDefinition for name" + beanName);
    }
    Class<?> targetClass = beanDefinition.getTargetClass();
    if (targetClass == null) {
      throw new BeanCreationException("BeanDefinition for bean name" + beanName + "should contain target class");
    }
    for (Constructor<?> constructor : targetClass.getConstructors()) {
      if (constructor.getParameterCount() == 0) {
        Object beanInstance = constructor.newInstance();
        log.info("Bean with name {} successfully created", beanName);
        return beanInstance;
      }
    }
    throw new BeanCreationException("Bean with name " + beanName + "does not have constructor without arguments");
  }

  /**
   * This method will BeanFactoryPostProcessor from @param postProcessor to existed bean factory post processors
   * Please note we can't have 2 the same processors
   */
  @Override
  public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
    if (postProcessor == null) {
      throw new IllegalArgumentException("BeanFactoryPostProcessor should not be null");
    }
    this.beanFactoryPostProcessors.add(postProcessor);
  }

  /**
   * This method will BeanPostProcessor from @param postProcessor to existed bean factory post processors
   * Please note we can't have 2 the same processors
   */
  @Override
  public void addBeanPostProcessor(BeanPostProcessor postProcessor) {
    if (postProcessor == null) {
      throw new IllegalArgumentException("BeanPostProcessor should not be null");
    }
    beanPostProcessors.add(postProcessor);
  }

  /**
   * this method will return existed BeanDefinition with selected beanName
   */
  @Override
  public BeanDefinition getBeanDefinitionByBeanName(String beanName) {
    BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
    if (beanDefinition == null) {
      log.warn("Can't find BeanDefinition with name {}", beanName);
    }
    return beanDefinition;
  }

  /**
   * this method will return all registered bean definition
   */
  @Override
  public Collection<BeanDefinition> getRegisteredBeanDefinitions() {
    return beanDefinitionRegistry.getAllBeanDefinitions();
  }

}
