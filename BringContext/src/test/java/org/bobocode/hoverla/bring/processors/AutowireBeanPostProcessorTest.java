package org.bobocode.hoverla.bring.processors;

import java.lang.reflect.Field;

import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.bean.BeanScope;
import org.bobocode.hoverla.bring.context.DefaultBeanDefinitionRegistry;
import org.bobocode.hoverla.bring.exception.BeanInjectionException;
import org.bobocode.hoverla.bring.factory.BeanFactory;
import org.bobocode.hoverla.bring.factory.BeanFactoryImpl;
import org.bobocode.hoverla.bring.objects.MySecondServiceImpl;
import org.bobocode.hoverla.bring.objects.MyService;
import org.bobocode.hoverla.bring.objects.MyServiceImpl;
import org.bobocode.hoverla.bring.objects.TestComponent;
import org.bobocode.hoverla.bring.objects.TestFirstService;
import org.bobocode.hoverla.bring.objects.TestSecondService;
import org.bobocode.hoverla.bring.objects.TestThirdService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AutowireBeanPostProcessorTest {

  private AutowireBeanPostProcessor autowireBeanPostProcessor;
  private BeanFactory beanFactory;

  private void init() {
    this.beanFactory = new BeanFactoryImpl();
    this.autowireBeanPostProcessor = new AutowireBeanPostProcessor(beanFactory);
  }

  //todo move this test to beanFactory
  @Test
  public void postProcessBean() {
    init();
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
    ;
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
  public void postProcessBean_dependenciesShouldBeInjectedWithBeanFromContext() {
    init();
    String firstServiceBeanName = "testFirstService";
    String secondServiceBeanName = "testSecondService";
    String thirdServiceBeanName = "testThirdService";
    DefaultBeanDefinitionRegistry defaultBeanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
    TestFirstService testFirstService = new TestFirstService();

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

    autowireBeanPostProcessor.postProcessBean(testFirstService, firstServiceBeanName);

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
  public void postProcessBean_exceptionShouldThrowWhenNoBeanDefinitionFoundForGivenBeanEvenIfThereAreBeanDefinitionForFieldMarkedWithAutowireAnnotation() {
    init();
    String firstServiceBeanName = "testFirstService";
    String testSecondServiceNamw ="second service";
    DefaultBeanDefinitionRegistry defaultBeanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
    TestFirstService testFirstService = new TestFirstService();

    BeanDefinition testSecondServiceBeanDefinition = new BeanDefinition();
    testSecondServiceBeanDefinition.setBeanName(testSecondServiceNamw);
    testSecondServiceBeanDefinition.setTargetClass(TestSecondService.class);
    testSecondServiceBeanDefinition.setScope(BeanScope.SINGLETON);

    defaultBeanDefinitionRegistry.registerBeanDefinition(testSecondServiceNamw, testSecondServiceBeanDefinition);

    beanFactory.setBeanDefinitionRegistry(defaultBeanDefinitionRegistry);
    beanFactory.addBeanPostProcessor(new AutowireBeanPostProcessor(beanFactory));

    assertThrows(BeanInjectionException.class, () -> autowireBeanPostProcessor.postProcessBean(testFirstService, firstServiceBeanName));
  }

  @Test
  public void postProcessBean_exceptionShouldThrowWhenNoBeanDefinitionFoundForClassToInject() {
    init();
    String firstServiceBeanName = "testFirstService";
    DefaultBeanDefinitionRegistry defaultBeanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
    TestFirstService testFirstService = new TestFirstService();

    BeanDefinition testFirstServiceBeanDefinition = new BeanDefinition();
    testFirstServiceBeanDefinition.setBeanName(firstServiceBeanName);
    testFirstServiceBeanDefinition.setTargetClass(TestFirstService.class);
    testFirstServiceBeanDefinition.setScope(BeanScope.SINGLETON);

    defaultBeanDefinitionRegistry.registerBeanDefinition(firstServiceBeanName, testFirstServiceBeanDefinition);

    beanFactory.setBeanDefinitionRegistry(defaultBeanDefinitionRegistry);
    beanFactory.addBeanPostProcessor(new AutowireBeanPostProcessor(beanFactory));

    assertThrows(BeanInjectionException.class, () -> autowireBeanPostProcessor.postProcessBean(testFirstService, firstServiceBeanName));
  }

  @Test
  public void postProcessBean_autowireByInterfaceShouldWorkCorrect() {
    init();
    String componentName = "componentName";
    String mySecondServiceImpl = "testMyComponent";

    DefaultBeanDefinitionRegistry defaultBeanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
    TestComponent testComponent = new TestComponent();

    BeanDefinition testComponentBeanDefinition = new BeanDefinition();
    testComponentBeanDefinition.setBeanName(componentName);
    testComponentBeanDefinition.setTargetClass(TestComponent.class);
    testComponentBeanDefinition.setScope(BeanScope.SINGLETON);
    defaultBeanDefinitionRegistry.registerBeanDefinition(componentName, testComponentBeanDefinition);

    BeanDefinition myServiceImpl = new BeanDefinition();
    myServiceImpl.setBeanName(mySecondServiceImpl);
    myServiceImpl.setTargetClass(MyServiceImpl.class);
    myServiceImpl.setScope(BeanScope.SINGLETON);
    defaultBeanDefinitionRegistry.registerBeanDefinition(mySecondServiceImpl, myServiceImpl);

    beanFactory.setBeanDefinitionRegistry(defaultBeanDefinitionRegistry);
    beanFactory.addBeanPostProcessor(new AutowireBeanPostProcessor(beanFactory));

    TestComponent withInjectedDependencies = (TestComponent) autowireBeanPostProcessor.postProcessBean(testComponent, componentName);
    MyService myService = withInjectedDependencies.getMyService();
    MyServiceImpl fromBeanFactory = (MyServiceImpl) beanFactory.getBean(mySecondServiceImpl);
    assertEquals(myService, fromBeanFactory);
  }

  @Test
  public void postProcessBean_exceptionShouldThrowWhenOneOrMoreThanOneCandidateFound() {
    init();
    String componentName = "componentName";
    String myServiceImplName = "myServiceiImpl";
    String mySecondServiceImpl = "testMyComponent";

    DefaultBeanDefinitionRegistry defaultBeanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
    TestComponent testComponent = new TestComponent();

    BeanDefinition testComponentBeanDefinition = new BeanDefinition();
    testComponentBeanDefinition.setBeanName(myServiceImplName);
    testComponentBeanDefinition.setTargetClass(TestComponent.class);
    testComponentBeanDefinition.setScope(BeanScope.SINGLETON);
    defaultBeanDefinitionRegistry.registerBeanDefinition(componentName, testComponentBeanDefinition);

    BeanDefinition testFirstServiceBeanDefinition = new BeanDefinition();
    testFirstServiceBeanDefinition.setBeanName(myServiceImplName);
    testFirstServiceBeanDefinition.setTargetClass(MySecondServiceImpl.class);
    testFirstServiceBeanDefinition.setScope(BeanScope.SINGLETON);
    defaultBeanDefinitionRegistry.registerBeanDefinition(myServiceImplName, testFirstServiceBeanDefinition);

    BeanDefinition myServiceImpl = new BeanDefinition();
    myServiceImpl.setBeanName(mySecondServiceImpl);
    myServiceImpl.setTargetClass(MyServiceImpl.class);
    myServiceImpl.setScope(BeanScope.SINGLETON);
    defaultBeanDefinitionRegistry.registerBeanDefinition(mySecondServiceImpl, myServiceImpl);

    beanFactory.setBeanDefinitionRegistry(defaultBeanDefinitionRegistry);
    beanFactory.addBeanPostProcessor(new AutowireBeanPostProcessor(beanFactory));

    assertThrows(BeanInjectionException.class, () -> autowireBeanPostProcessor.postProcessBean(testComponent, componentName));
  }
}