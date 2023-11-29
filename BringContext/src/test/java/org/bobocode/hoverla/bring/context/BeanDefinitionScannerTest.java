package org.bobocode.hoverla.bring.context;

import java.util.List;
import java.util.Set;

import org.bobocode.hoverla.bring.annotations.Component;
import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.bean.BeanScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BeanDefinitionScannerTest {

  private BeanDefinitionScanner scanner;

  @BeforeEach
  public void setUp() {
    scanner = new BeanDefinitionScannerImpl(new DefaultBeanDefinitionRegistry(), Set.of(Component.class));
  }

  @Test
  void testLoadBeanDefinitionsFindsInnerComponent() {
    var expected = BeanDefinition.of(TestService.class.getSimpleName(), TestService.class, BeanScope.SINGLETON);

    String basePackage = "org.bobocode.hoverla.bring.context";

    List<BeanDefinition> beanDefinitions = scanner.loadBeanDefinitions(basePackage);

    assertFalse(beanDefinitions.isEmpty());
    assertEquals(1, beanDefinitions.size());

    var actual = beanDefinitions.get(0);

    assertEquals(expected, actual);
  }

  @Test
  void testLoadBeanDefinitionsFindsNoComponents() {
    String basePackage = "org.bobocode.hoverla.bring.utils";

    List<BeanDefinition> beanDefinitions = scanner.loadBeanDefinitions(basePackage);

    assertTrue(beanDefinitions.isEmpty(), "Should find no components in an empty package");
  }

  @Test
  void testLoadBeanDefinitionsInvalidPackage() {
    String basePackage = "org.bobocode.hoverla.invalidPackage";

    List<BeanDefinition> beanDefinitions = scanner.loadBeanDefinitions(basePackage);

    assertTrue(beanDefinitions.isEmpty(), "Should find no components in an empty package");
  }

  @Component
  public static class TestService {
  }

}