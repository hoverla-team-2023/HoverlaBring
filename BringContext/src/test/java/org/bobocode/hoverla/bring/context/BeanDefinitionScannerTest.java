package org.bobocode.hoverla.bring.context;


import org.bobocode.hoverla.bring.annotations.Component;
import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.bean.BeanScope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BeanDefinitionScannerTest {
    @Mock
    private BeanDefinitionRegistry mockRegistry;

    private BeanDefinitionScanner scanner;

    @BeforeEach
    public void setUp() {
        scanner = new BeanDefinitionScannerImpl();
        scanner.registerBeanDefinitions(mockRegistry);
    }

    @Test
    public void testLoadBeanDefinitionsFindsInnerComponent() {
        var expected = BeanDefinition.of(TestService.class.getSimpleName(), TestService.class, BeanScope.SINGLETON);

        String basePackage = "org.bobocode.hoverla";

        List<BeanDefinition> beanDefinitions = scanner.loadBeanDefinitions(basePackage);

        assertFalse(beanDefinitions.isEmpty());
        assertEquals(beanDefinitions.size(), 1);

        var actual = beanDefinitions.get(0);

        assertEquals(expected, actual);
    }

    @Test
    public void testLoadBeanDefinitionsFindsNoComponents() {
        String basePackage = "org.bobocode.hoverla.bring.utils";

        List<BeanDefinition> beanDefinitions = scanner.loadBeanDefinitions(basePackage);

        assertTrue(beanDefinitions.isEmpty(), "Should find no components in an empty package");
    }

    @Test
    public void testLoadBeanDefinitionsInvalidPackage() {
        String basePackage = "org.bobocode.hoverla.invalidPackage";

        List<BeanDefinition> beanDefinitions = scanner.loadBeanDefinitions(basePackage);

        assertTrue(beanDefinitions.isEmpty(), "Should find no components in an empty package");
    }


    @Component
    public static class TestService {
    }
}