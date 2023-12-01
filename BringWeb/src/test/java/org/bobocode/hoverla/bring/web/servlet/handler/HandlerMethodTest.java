package org.bobocode.hoverla.bring.web.servlet.handler;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HandlerMethodTest {

  @Test
  void handleRequest_shouldInvokeHandlerMethodSuccessfully() throws Exception {
    // Create a sample method for testing
    Method sampleMethod = SampleController.class.getMethod("sampleMethod", String.class);

    // Create a sample HandlerMethod
    HandlerMethod handlerMethod = HandlerMethod.builder()
      .beanType(SampleController.class)
      .method(sampleMethod)
      .parameters(sampleMethod.getParameters())
      .bean(new SampleController())
      .build();

    // Create sample arguments
    String argument = "testArgument";

    // Act
    Object result = handlerMethod.handleRequest(argument);

    // Assert
    assertEquals("SampleResult", result);

  }

  // Sample controller class for testing
  static class SampleController {

    public String sampleMethod(String arg) {
      return "SampleResult";
    }

  }

}