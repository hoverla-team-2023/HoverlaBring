package org.bobocode.hoverla.bring.factory;

import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.bean.BeanScope;
import org.bobocode.hoverla.bring.context.BeanDefinitionRegistry;
import org.bobocode.hoverla.bring.context.DefaultBeanDefinitionRegistry;
import org.bobocode.hoverla.bring.exception.BeanCreationException;
import org.bobocode.hoverla.bring.objects.TestFirstService;
import org.bobocode.hoverla.bring.objects.TestSecondService;
import org.bobocode.hoverla.bring.objects.TestThirdService;
import org.bobocode.hoverla.bring.processors.AutowireBeanPostProcessor;
import org.bobocode.hoverla.bring.processors.BeanFactoryPostProcessor;
import org.bobocode.hoverla.bring.processors.BeanPostProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeanFactoryImplTest {

  private BeanFactoryImpl beanFactory;
  private BeanDefinitionRegistry beanDefinitionRegistryMock;

  @BeforeEach
  void setUp() {
    beanFactory = new BeanFactoryImpl();
    beanDefinitionRegistryMock = Mockito.mock(BeanDefinitionRegistry.class);
    beanFactory.setBeanDefinitionRegistry(beanDefinitionRegistryMock);
  }

  @Test
  public void postProcessBean() {
    String firstServiceBeanName = "testFirstService";
    String secondServiceBeanName = "testSecondService";
    String thirdServiceBeanName = "testThirdService";
    DefaultBeanDefinitionRegistry defaultBeanDefinitionRegistry = new DefaultBeanDefinitionRegistry();

    BeanDefinition testFirstServiceBeanDefinition = new BeanDefinition();
    testFirstServiceBeanDefinition.setBeanName(firstServiceBeanName);
    testFirstServiceBeanDefinition.setTargetClass(TestFirstService.class);
    testFirstServiceBeanDefinition.setScope(BeanScope.SINGLETON);
    defaultBeanDefinitionRegistry.registerBeanDefinition(firstServiceBeanName, testFirstServiceBeanDefinition);

    BeanDefinition testSecondServiceBeanDefinition = new BeanDefinition();
    testSecondServiceBeanDefinition.setBeanName(secondServiceBeanName);
    testSecondServiceBeanDefinition.setTargetClass(TestSecondService.class);
    testSecondServiceBeanDefinition.setScope(BeanScope.SINGLETON);
    defaultBeanDefinitionRegistry.registerBeanDefinition(secondServiceBeanName, testSecondServiceBeanDefinition);

    BeanDefinition testThirdServiceBeanDefinition = new BeanDefinition();
    testThirdServiceBeanDefinition.setBeanName(thirdServiceBeanName);
    testThirdServiceBeanDefinition.setTargetClass(TestThirdService.class);
    testThirdServiceBeanDefinition.setScope(BeanScope.SINGLETON);

    defaultBeanDefinitionRegistry.registerBeanDefinition(thirdServiceBeanName, testThirdServiceBeanDefinition);

    beanFactory.setBeanDefinitionRegistry(defaultBeanDefinitionRegistry);
    beanFactory.addBeanPostProcessor(new AutowireBeanPostProcessor(beanFactory));
    beanFactory.tryToInitializeSingletonBean(firstServiceBeanName);

    TestFirstService testFirstServiceBean = (TestFirstService) beanFactory.getBean(firstServiceBeanName);
    TestSecondService testSecondServiceBean = (TestSecondService) beanFactory.getBean(secondServiceBeanName);
    TestThirdService testThirdServiceBean = (TestThirdService) beanFactory.getBean(thirdServiceBeanName);

    assertNotNull(testFirstServiceBean);
    assertNotNull(testSecondServiceBean);
    assertNotNull(testThirdServiceBean);

    assertSame(testFirstServiceBean.getTestSecondService(), testSecondServiceBean);
    assertSame(testSecondServiceBean.getTestThirdService(), testThirdServiceBean);
  }

  @Test
  void AddBeanFactoryPostProcessor() {
    // Mocking
    BeanFactoryPostProcessor postProcessorMock = Mockito.mock(BeanFactoryPostProcessor.class);

    // Testing
    beanFactory.addBeanFactoryPostProcessor(postProcessorMock);
  }

  @Test
  void AddBeanPostProcessor() {
    // Mocking
    BeanPostProcessor postProcessorMock = Mockito.mock(BeanPostProcessor.class);

    // Testing
    beanFactory.addBeanPostProcessor(postProcessorMock);
  }

  @Test
  void GetBeanForUnknownBean() {
    // Mocking
    String beanName = "unknownBean";
    when(beanDefinitionRegistryMock.getBeanDefinition(beanName)).thenReturn(null);

    // Testing
    assertThrows(BeanCreationException.class, () -> beanFactory.getBean(beanName));
  }

  @Test
  void GetBeanForNonExistentTargetClass() {
    // Mocking
    String beanName = "testBean";
    BeanDefinition beanDefinitionMock = Mockito.mock(BeanDefinition.class);
    when(beanDefinitionMock.getScope()).thenReturn(BeanScope.SINGLETON);
    when(beanDefinitionRegistryMock.getBeanDefinition(beanName)).thenReturn(beanDefinitionMock);

    // Testing
    when(beanDefinitionMock.getTargetClass()).thenReturn(null);
    assertThrows(BeanCreationException.class, () -> beanFactory.getBean(beanName));
  }

  @Test
  void GetBeanForNonInstantiableClass() {
    // Mocking
    String beanName = "testBean";
    BeanDefinition beanDefinitionMock = Mockito.mock(BeanDefinition.class);
    when(beanDefinitionMock.getScope()).thenReturn(BeanScope.SINGLETON);
    when(beanDefinitionRegistryMock.getBeanDefinition(beanName)).thenReturn(beanDefinitionMock);

    // Testing
    when(beanDefinitionMock.getTargetClass()).thenReturn((Class) NonInstantiableClass.class);
    assertThrows(BeanCreationException.class, () -> beanFactory.getBean(beanName));
  }

  static class NonInstantiableClass {

    private NonInstantiableClass() {
    }

  }

}