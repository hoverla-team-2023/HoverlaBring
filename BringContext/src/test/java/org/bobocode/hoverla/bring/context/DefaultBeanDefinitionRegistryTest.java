package org.bobocode.hoverla.bring.context;

import java.util.Collection;
import java.util.Set;

import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DefaultBeanDefinitionRegistryTest {

  @InjectMocks
  private DefaultBeanDefinitionRegistry registry;

  @Test
  void registerBeanDefinition_registerBeanDefinitionWithNullBeanNameShouldThrowException() {
    BeanDefinition beanDefinition = new BeanDefinition();

    assertThrows(IllegalArgumentException.class, () -> registry.registerBeanDefinition(null, beanDefinition));
  }

  @Test
  void registerBeanDefinition_registerBeanDefinitionWithNullBeanDefinitionShouldThrowException() {
    String beanName = "some name";

    assertThrows(IllegalArgumentException.class, () -> registry.registerBeanDefinition(beanName, null));
  }
  @Test
  void registerBeanDefinition_registerBeanThatDoesNotExist() {
    String beanDefinitionName = "my super puper bean";
    BeanDefinition beanDefinition = new BeanDefinition();
    beanDefinition.setBeanName(beanDefinitionName);

    registry.registerBeanDefinition(beanDefinitionName, beanDefinition);

    BeanDefinition registeredBeanDefinition = registry.getBeanDefinition(beanDefinitionName);

    assertNotNull(registeredBeanDefinition);
  }

  @Test
  void registerBeanDefinition_getBeanDefinitionShouldThrowErrorWhenBeanDefinitionNameIsNull() {
    assertThrows(IllegalArgumentException.class,()-> registry.getBeanDefinition(null));
  }

  @Test
  void registerBeanDefinition_registerBeanThatExistShouldThrowException() {
    String beanDefinitionName = "my super puper bean";
    BeanDefinition beanDefinition = new BeanDefinition();
    BeanDefinition beanDefinitionWithTheSameName = new BeanDefinition();
    beanDefinition.setBeanName(beanDefinitionName);
    beanDefinitionWithTheSameName.setBeanName(beanDefinitionName);

    registry.registerBeanDefinition(beanDefinitionName, beanDefinition);
    assertThrows(IllegalArgumentException.class, () -> registry.registerBeanDefinition(beanDefinitionName, beanDefinition));
  }

  @Test
  void getAllBeanDefinitionNames() {
    String beanDefinitionName = "my super puper bean";
    BeanDefinition beanDefinition = new BeanDefinition();
    BeanDefinition beanDefinitionWithTheSameName = new BeanDefinition();
    beanDefinition.setBeanName(beanDefinitionName);
    beanDefinitionWithTheSameName.setBeanName(beanDefinitionName);

    registry.registerBeanDefinition(beanDefinitionName, beanDefinition);

    Set<String> allBeanDefinitionNames = registry.getAllBeanDefinitionNames();

    assertEquals(1, allBeanDefinitionNames.size());
    assertTrue(allBeanDefinitionNames.contains(beanDefinitionName));
  }

  @Test
  void getAllBeanDefinitions() {
    String beanDefinitionName = "my super puper bean";
    BeanDefinition beanDefinition = new BeanDefinition();
    BeanDefinition beanDefinitionWithTheSameName = new BeanDefinition();
    beanDefinition.setBeanName(beanDefinitionName);
    beanDefinitionWithTheSameName.setBeanName(beanDefinitionName);

    registry.registerBeanDefinition(beanDefinitionName, beanDefinition);

    Collection<BeanDefinition> allBeanDefinitions = registry.getAllBeanDefinitions();

    assertEquals(1, allBeanDefinitions.size());
    assertTrue(allBeanDefinitions.contains(beanDefinition));
  }

}