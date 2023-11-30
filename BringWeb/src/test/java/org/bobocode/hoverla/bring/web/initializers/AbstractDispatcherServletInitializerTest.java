package org.bobocode.hoverla.bring.web.initializers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;

import org.bobocode.hoverla.bring.web.servlet.DispatcherServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AbstractDispatcherServletInitializerTest {

  private AbstractDispatcherServletInitializer instance;

  @Captor
  private ArgumentCaptor<DispatcherServlet> dispatcherServletCaptor;

  @BeforeEach
  void setup() {
    instance = new MockAbstractDispatcherServletInitializer();
  }

  @Test
  void givenServletContext_whenOnStartup_thenRegisterDispatcherServlet(@Mock ServletContext servletContext, @Mock ServletRegistration.Dynamic registration) {
    doReturn(registration).when(servletContext).addServlet(eq("dispatcher"), any(DispatcherServlet.class));

    instance.onStartup(servletContext);

    verify(servletContext).addServlet(eq("dispatcher"), dispatcherServletCaptor.capture());

    var dispatcherServlet = dispatcherServletCaptor.getValue();
    assertEquals(servletContext, dispatcherServlet.getServletContext());

    verify(registration).setLoadOnStartup(1);
    verify(registration).setAsyncSupported(true);
    verify(registration).addMapping("/");
  }

  private static class MockAbstractDispatcherServletInitializer extends AbstractDispatcherServletInitializer {

    @Override
    protected String getPackagesToScan() {
      return "org.bobocode.hoverla.bring.web.initializers";
    }

  }

}