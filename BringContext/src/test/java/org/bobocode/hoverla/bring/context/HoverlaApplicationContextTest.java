package org.bobocode.hoverla.bring.context;

import org.bobocode.hoverla.bring.objects.DbConnection;
import org.bobocode.hoverla.bring.objects.MyService;
import org.bobocode.hoverla.bring.objects.MyServiceImpl;
import org.bobocode.hoverla.bring.objects.TestComponent;
import org.bobocode.hoverla.bring.objects.TestFirstService;
import org.bobocode.hoverla.bring.objects.TestSecondService;
import org.bobocode.hoverla.bring.objects.TestThirdService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.bobocode.hoverla.bring.utils.BeanUtils.generateBeanName;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class HoverlaApplicationContextTest {

  @Test
  void shouldInitializeApplicationContextWithGivenPath() {
    HoverlaApplicationContext context = new HoverlaApplicationContext("com.example.package");
    assertNotNull(context);
  }

  @Test
  void shouldInitializeApplicationContextWithDefaultPath() {
    HoverlaApplicationContext context = new HoverlaApplicationContext("default/path");
    assertNotNull(context);
  }

  @Test
  void shouldCreateBeansFromComponentAnnotatedClasses() {
    HoverlaApplicationContext context = new HoverlaApplicationContext("org.bobocode.hoverla.bring.objects");
    MyServiceImpl myService = (MyServiceImpl) context.getBeanFactory().getBean(generateBeanName(MyServiceImpl.class));
    TestComponent testComponent = (TestComponent) context.getBeanFactory().getBean(generateBeanName(TestComponent.class));
    TestFirstService testFirstService = (TestFirstService) context.getBeanFactory().getBean(generateBeanName(TestFirstService.class));
    TestSecondService testSecondService = (TestSecondService) context.getBeanFactory().getBean(generateBeanName(TestSecondService.class));
    TestThirdService testThirdService = (TestThirdService) context.getBeanFactory().getBean(generateBeanName(TestThirdService.class));
    assertNotNull(myService);
    assertNotNull(testComponent);
    assertNotNull(testFirstService);
    assertNotNull(testSecondService);
    assertNotNull(testThirdService);
    assertSame(testComponent.getMyService(), myService);
    assertSame(testFirstService.getTestSecondService().getTestThirdService(), testThirdService);
  }

  @Test
  void shouldInjectDependenciesWithAutowireAnnotation() {
    HoverlaApplicationContext context = new HoverlaApplicationContext("org.bobocode.hoverla.bring.objects");
    TestComponent testComponent = (TestComponent) context.getBeanFactory().getBean(generateBeanName(TestComponent.class));
    MyService myService = (MyService) context.getBeanFactory().getBean(generateBeanName(MyServiceImpl.class));
    assertSame(testComponent.getMyService(), myService);

  }

  @Test
  void shouldGetBeanByInterfaceIfComponentThatImplementsGivenInterfaceExists() {
    HoverlaApplicationContext context = new HoverlaApplicationContext("org.bobocode.hoverla.bring.objects");
    Object byInterface = context.getBean(MyService.class);
    Object byImplementedClassName = context.getBean(generateBeanName(MyServiceImpl.class));
    assertTrue(byInterface == byImplementedClassName);
  }

  @Test
  void shouldThrowExceptionForNonExistingComponent() {
    HoverlaApplicationContext context = new HoverlaApplicationContext("org.bobocode.hoverla.bring.objects");
    assertThrows(IllegalArgumentException.class, () -> context.getBean(Test.class));
  }

  @Test
  void shouldThrowExceptionIfMoreThanOneCandidateExist() {
    HoverlaApplicationContext context = new HoverlaApplicationContext("org.bobocode.hoverla.bring.objects");
    assertThrows(IllegalArgumentException.class, () -> context.getBean(DbConnection.class));
  }

}
