package org.bobocode.hoverla.bring.processors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.bobocode.hoverla.bring.annotations.Autowired;
import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.exception.BeanInjectionException;
import org.bobocode.hoverla.bring.factory.BeanFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutowireBeanPostProcessor implements BeanPostProcessor {

  private BeanFactory beanFactory;

  List<String> candidateNames = new ArrayList<>();

  public AutowireBeanPostProcessor(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  //todo if we decide to have some proxy for beam in init() method use this for saving original bean or bean class, else remove this method and rename postProcessAfterInitialization to postProcessBean
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    BeanDefinition beanDefinitionByBeanName = beanFactory.getBeanDefinitionByBeanName(beanName);
    Class<?> targetClass = beanDefinitionByBeanName.getTargetClass();
    if (targetClass.getDeclaredFields().length > 0) {
      for (Field field : targetClass.getDeclaredFields()) {
        if (field.isAnnotationPresent(Autowired.class)) {
          BeanDefinition candidate = defineRequiredBeanBeanDefinition(beanDefinitionByBeanName, field);
          Object beanToInject = beanFactory.getBean(candidate.getBeanName());
          field.setAccessible(true);
          try {
            ReflectionUtil.setFieldValue(field, bean, beanToInject);
          } catch (IllegalArgumentException e) {
            throw new BeanInjectionException("Failed to inject field " + field.getName() + " to bean " + beanName + ". Candidate object " + bean, e);
          }
        }
      }
    } else {
      log.debug("No fields to inject found for beam with name: {}", beanName);
    }
    return bean;
  }

  private BeanDefinition defineRequiredBeanBeanDefinition(BeanDefinition beanDefinition, Field injectionCandidate) {
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
