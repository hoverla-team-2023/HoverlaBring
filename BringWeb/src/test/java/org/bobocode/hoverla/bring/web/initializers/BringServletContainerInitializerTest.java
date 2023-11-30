package org.bobocode.hoverla.bring.web.initializers;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Stream;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import org.bobocode.hoverla.bring.web.exceptions.NoServletInitializerPresentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.NoArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BringServletContainerInitializerTest {

  private BringServletContainerInitializer instance;

  @BeforeEach
  void setup() {
    instance = new BringServletContainerInitializer();
  }

  /**
   * Valid initializer with a public default constructor.
   */
  @NoArgsConstructor
  private static class ValidDispatcherServletInitializer extends AbstractDispatcherServletInitializer {

    @Override
    protected String getPackagesToScan() {
      return "org.bobocode.hoverla.bring.web.initializers";
    }

  }

  @Test
  void givenServletInitializersPresent_whenOnStartup_thenStartEachServletInitializer(@Mock ServletContext servletContext) throws ServletException {
    try (var initializer = mockConstruction(ValidDispatcherServletInitializer.class)) {
      instance.onStartup(Set.of(ValidDispatcherServletInitializer.class), servletContext);

      verify(initializer.constructed().get(0)).onStartup(servletContext);
    }
  }

  @Test
  void givenNoServletInitializersPresent_whenOnStartup_thenThrowNoServletInitializerPresentException(@Mock ServletContext servletContext) {
    Set<Class<?>> initializers = Set.of(ServletInitializer.class, AbstractDispatcherServletInitializer.class);
    var expected = "No ServletInitializer found in the classpath. Consider extending the AbstractDispatcherServletInitializer to create a dispatcher servlet.";

    var result = assertThrows(NoServletInitializerPresentException.class, () -> instance.onStartup(initializers, servletContext));
    assertEquals(expected, result.getMessage());
  }

  static Stream<Arguments> instantiationExceptions() {
    return Stream.of(
      arguments(new InstantiationException()),
      arguments(new IllegalAccessException()),
      arguments(new InvocationTargetException(null)),
      arguments(new NoSuchMethodException())
    );
  }

  @ParameterizedTest
  @MethodSource("instantiationExceptions")
  void givenExceptionThrowWhileInitializerInstantiation_whenOnStartup_thenThrowServletException(Throwable exception, @Mock ServletContext servletContext) {
    var initializerClass = ValidDispatcherServletInitializer.class;
    var message = "Unable to create instance of class %s. Please make sure that the class has a public default constructor".formatted(
      initializerClass.getName());

    try (var initializer = mockConstruction(initializerClass, (mock, context) -> {
      throw exception;
    })) {
      var result = assertThrows(ServletException.class, () -> instance.onStartup(Set.of(initializerClass), servletContext));

      assertEquals(message, result.getMessage());
      assertTrue(initializer.constructed().isEmpty());
    }
  }

}