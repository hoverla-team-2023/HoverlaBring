package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.bobocode.hoverla.bring.web.annotations.PathVariable;
import org.bobocode.hoverla.bring.web.annotations.RequestMapping;
import org.bobocode.hoverla.bring.web.annotations.RequestMethod;
import org.bobocode.hoverla.bring.web.servlet.handler.HandlerMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class PathVariableArgumentResolverTest {

  private PathVariableArgumentResolver resolver;
  private HttpServletRequest request;
  private HttpServletResponse response;
  private HandlerMethod handlerMethod;

  @BeforeEach
  public void setUp() {
    resolver = new PathVariableArgumentResolver();
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    handlerMethod = mock(HandlerMethod.class);
  }

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
  public void supportsParameterShouldReturnTrueForAnnotatedParameter() throws NoSuchMethodException {
    Method stringPathVariableMethod = TestController.class.getMethod("stringPathVariableMethod", String.class);
    Parameter annotatedParameter = stringPathVariableMethod.getParameters()[0];

    boolean result = resolver.supportsParameter(annotatedParameter);

    assertTrue(result);
  }

  @Test
  public void resolveArgumentShouldReturnProperlyParsedValue() throws NoSuchMethodException {
    Method method = TestController.class.getMethod("method", String.class);
    Parameter annotatedParameter = method.getParameters()[0];

    when(handlerMethod.getPath()).thenReturn("/example/{id}/test");
    when(request.getRequestURI()).thenReturn("/example/123/test");

    String result = (String) resolver.resolveArgument(handlerMethod, annotatedParameter, request, response);

    assertEquals("123", result);
  }

  @Test
  void resolveArgument_multiplePathVariables() throws NoSuchMethodException {
    Method exampleMethod = TestController.class.getMethod("exampleMethod", String.class, Long.class);

    when(handlerMethod.getPath()).thenReturn("/conversations/{conversationId}/notes/{noteId}");
    when(request.getRequestURI()).thenReturn("/conversations/123/notes/456");

    String conversationIdResult = (String) resolver.resolveArgument(handlerMethod, exampleMethod.getParameters()[0], request, response);
    Long noteIdResult = (Long) resolver.resolveArgument(handlerMethod, exampleMethod.getParameters()[1], request, response);

    assertEquals("123", conversationIdResult);
    assertEquals(456L, noteIdResult);
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

    @RequestMapping(path = "/example/{id}/test", method = RequestMethod.GET)
    public void method(@PathVariable("id") String id) {
    }

    @RequestMapping(path = "/conversations/{conversationId}/notes/{noteId}", method = RequestMethod.POST)
    public void exampleMethod(@PathVariable("conversationId") String conversationId, @PathVariable("noteId") Long noteId) {

    }

  }

}


