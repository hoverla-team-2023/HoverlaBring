package org.bobocode.hoverla.bring.processors;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.bobocode.hoverla.bring.annotations.Autowired;
import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.exception.BeanInjectionException;
import org.bobocode.hoverla.bring.factory.BeanFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * This implementation of BeanPostProcessor is responsible for dependency injection process.
 * It scans all recently created beans find fields marked with @Autowire annotation find required bean in BeanFactory and then inject dependency into given bean
 * please note if no required data(beans or beanDefinition) to inject found you will accept BeanCreation exception
 */
@Slf4j
public class AutowireBeanPostProcessor implements BeanPostProcessor {

  private BeanFactory beanFactory;

  public AutowireBeanPostProcessor(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  /**
   * This method will inject all required dependency to bean and return it
   * Please note, this method will inject fields by interface too:
   * Example we have class UserServiceImpl that implements UserService, you can inject it by two ways @Autowired private UserService userService
   * and @Autowired private UserServiceImpl userService;
   * But at the moment if u have 2 classes that is marked as @Component and you try to autowire by UserService excption will be throw
   *
   * @param bean     object to inject
   * @param beanName keyword used to find bean in BeanFactory
   *
   * @return updated bean
   */
  @Override
  public Object postProcessBean(Object bean, String beanName) {
    BeanDefinition beanDefinitionByBeanName = beanFactory.getBeanDefinitionByBeanName(beanName);
    log.debug("Found next bean definition {} for bean with name {}", beanDefinitionByBeanName, beanName);
    if (beanDefinitionByBeanName == null) {
      throw new BeanInjectionException("Failed to inject dependency for bean with name" + beanName + " bean definition exists");
    }
    Class<?> targetClass = beanDefinitionByBeanName.getTargetClass();
    if (targetClass.getDeclaredFields().length > 0) {
      for (Field field : targetClass.getDeclaredFields()) {
        if (field.isAnnotationPresent(Autowired.class)) {
          injectDependency(bean, beanName, beanDefinitionByBeanName, field);
        }
      }
    } else {
      log.debug("No fields to inject found for beam with name: {}", beanName);
    }
    return bean;
  }

  void injectDependency(Object bean, String beanName, BeanDefinition beanDefinitionByBeanName, Field field) {
    BeanDefinition candidate = defineRequiredBeanBeanDefinition(beanDefinitionByBeanName, field);
    log.debug("Found next candidate {} to inject into field {} for bean with name {} ", candidate.getBeanName(), field.getName(), beanName);
    Object beanToInject = beanFactory.getBean(candidate.getBeanName());
    if (beanToInject == null) {
      throw new BeanInjectionException(
        "Failed to inject field " + field.getName() + " to bean " + beanName + ". Candidate object " + bean + " no required bean to inject found");
    }
    try {
      field.setAccessible(true);
      ReflectionUtil.setFieldValue(field, bean, beanToInject);
    } catch (IllegalArgumentException e) {
      throw new BeanInjectionException("Failed to inject field " + field.getName() + " to bean " + beanName + ". Candidate object " + bean, e);
    }
  }

  BeanDefinition defineRequiredBeanBeanDefinition(BeanDefinition beanDefinition, Field injectionCandidate) {
    Collection<BeanDefinition> existedBeamDefinitions = beanFactory.getRegisteredBeanDefinitions();
    if (existedBeamDefinitions == null || existedBeamDefinitions.isEmpty()) {
      throw new BeanInjectionException("No bean definitions found ");
    }
    List<BeanDefinition> acceptableToInject = existedBeamDefinitions
      .stream()
      .filter(existed -> injectionCandidate.getType().isAssignableFrom(existed.getTargetClass())).toList();
    if (acceptableToInject.isEmpty()) {
      throw new BeanInjectionException("No acceptable BeanDefinition found for beam with name" + beanDefinition.getBeanName());
    }
    if (acceptableToInject.size() > 1) {
      throw new BeanInjectionException("More than 1 acceptable candidate found to inject for bean with name " + beanDefinition.getBeanName() +
                                       ". Injected field: " + injectionCandidate.getName() +
                                       ". Defined candidates:" + String.join(",", acceptableToInject.stream().map(BeanDefinition::getBeanName).toList()));
    }
    BeanDefinition toInject = acceptableToInject.get(0);
    log.debug("Defined bean with name {} to inject into field {} for bean {}", toInject.getBeanName(), injectionCandidate.getName(), toInject.getBeanName());
    return toInject;
  }

}
