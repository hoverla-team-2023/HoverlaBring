package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.annotations.PathVariable;
import org.bobocode.hoverla.bring.web.annotations.RequestMapping;
import org.bobocode.hoverla.bring.web.annotations.RequestMethod;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class PathVariableArgumentResolverTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  PathVariableArgumentResolver resolver = new PathVariableArgumentResolver();

  @Test
  void supportsParameter() throws NoSuchMethodException {
    Method stringPathVariableMethod = TestController.class.getMethod("stringPathVariableMethod", String.class);
    Method longPathVariableMethod = TestController.class.getMethod("longPathVariableMethod", Long.class);
    Method enumPathVariableMethod = TestController.class.getMethod("enumPathVariableMethod", TestEnum.class);

    assertTrue(resolver.supportsParameter(stringPathVariableMethod.getParameters()[0]));
    assertTrue(resolver.supportsParameter(longPathVariableMethod.getParameters()[0]));
    assertTrue(resolver.supportsParameter(enumPathVariableMethod.getParameters()[0]));

  }

  @Test
  void supportsParameter_multiplePathVariables() throws NoSuchMethodException {
    Method exampleMethod = TestController.class.getMethod("exampleMethod", String.class, Long.class);
    assertTrue(resolver.supportsParameter(exampleMethod.getParameters()[0]));
    assertTrue(resolver.supportsParameter(exampleMethod.getParameters()[1]));
  }

  @Test
  void resolveArgument() {
    Mockito.when(request.getParameter("stringValue")).thenReturn("TestString");
    Mockito.when(request.getParameter("longValue")).thenReturn("123");
    Mockito.when(request.getParameter("enumValue")).thenReturn("VALUE1");

    Method stringPathVariableMethod;
    try {
      stringPathVariableMethod = TestController.class.getMethod("stringPathVariableMethod", String.class);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    Method longPathVariableMethod;
    try {
      longPathVariableMethod = TestController.class.getMethod("longPathVariableMethod", Long.class);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    Method enumPathVariableMethod;
    try {
      enumPathVariableMethod = TestController.class.getMethod("enumPathVariableMethod", TestEnum.class);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }

    assertEquals("TestString", resolver.resolveArgument(stringPathVariableMethod.getParameters()[0], request, response));
    assertEquals(123L, resolver.resolveArgument(longPathVariableMethod.getParameters()[0], request, response));
    assertEquals(TestEnum.VALUE1, resolver.resolveArgument(enumPathVariableMethod.getParameters()[0], request, response));

  }

  @Test
  void resolveArgument_multiplePathVariables() {

    Mockito.when(request.getParameter("conversationId")).thenReturn("TestString");
    Mockito.when(request.getParameter("noteId")).thenReturn("123");

    Method exampleMethod;
    try {
      exampleMethod = TestController.class.getMethod("exampleMethod", String.class, Long.class);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }

    assertEquals("TestString", resolver.resolveArgument(exampleMethod.getParameters()[0], request, response));
    assertEquals(123L, resolver.resolveArgument(exampleMethod.getParameters()[1], request, response));
  }

  enum TestEnum {
    VALUE1,
    VALUE2
  }

  static class TestController {

    @RequestMapping(path = "/test/{stringValue}", method = RequestMethod.GET)
    public void stringPathVariableMethod(@PathVariable("stringValue") String stringValue) {

    }

    @RequestMapping(path = "/test/{longValue}", method = RequestMethod.POST)
    public void longPathVariableMethod(@PathVariable("longValue") Long longValue) {

    }

    @RequestMapping(path = "/test/{enumValue}", method = RequestMethod.PUT)
    public void enumPathVariableMethod(@PathVariable("enumValue") TestEnum enumValue) {

    }

    @RequestMapping(path = "/conversations/{conversationId}/notes/{noteId}", method = RequestMethod.POST)
    public void exampleMethod(@PathVariable("conversationId") String conversationId, @PathVariable("noteId") Long noteId) {

    }

  }

}


