package org.bobocode.hoverla.bring.web.servlet.resolver;

import java.util.List;
import java.util.Queue;
import java.util.Set;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.bobocode.hoverla.bring.web.exceptions.UnsupportedArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServletArgumentResolverTest {

  private ServletArgumentResolver instance;

  @BeforeEach
  void setup() {
    instance = new ServletArgumentResolver();
  }

  void mockMethod(HttpServletRequest request) {}

  @Test
  void givenHttpServletRequest_whenSupportsParameter_thenReturnTrue() throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", HttpServletRequest.class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertTrue(result);
  }

  void mockMethod(HttpServletResponse response) {}

  @Test
  void givenHttpServletResponse_whenSupportsParameter_thenReturnTrue() throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", HttpServletResponse.class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertTrue(result);
  }

  void mockMethod(HttpSession session) {}

  @Test
  void givenHttpSession_whenSupportsParameter_thenReturnTrue() throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", HttpSession.class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertTrue(result);
  }

  void mockMethod(ServletContext servletContext) {}

  @Test
  void givenServletContext_whenSupportsParameter_thenReturnTrue() throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", ServletContext.class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertTrue(result);
  }

  void mockMethod(Cookie[] cookies) {}

  @Test
  void givenCookieArray_whenSupportsParameter_thenReturnTrue() throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", Cookie[].class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertTrue(result);
  }

  void mockMethod(List<Cookie> cookies) {}

  @Test
  void givenCookieCollection_whenSupportsParameter_thenReturnTrue() throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", List.class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertTrue(result);
  }

  void mockMethodUnsupportedCollection(List<Object> list) {}

  @Test
  void givenNonCookieCollection_whenSupportsParameter_thenReturnFalse() throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethodUnsupportedCollection", List.class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertFalse(result);
  }

  void mockMethodRawTypeCollection(List list) {}

  @Test
  void givenRawTypeCollection_whenSupportsParameter_thenReturnFalse() throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethodRawTypeCollection", List.class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertFalse(result);
  }

  @Test
  void givenNullParameter_whenSupportsParameter_thenReturnFalse() {
    var result = instance.supportsParameter(null);
    assertFalse(result);
  }

  void mockMethod(Object object) {}

  @Test
  void givenUnsupportedParameterType_whenSupportsParameter_thenReturnFalse() throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", Object.class).getParameters()[0];

    var result = instance.supportsParameter(parameter);
    assertFalse(result);
  }

  @Test
  void givenHttpServletRequest_whenResolveArgument_thenReturnHttpServletRequest(@Mock HttpServletRequest request) throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", HttpServletRequest.class).getParameters()[0];

    var result = instance.resolveArgument(null, parameter, request, null);
    assertEquals(request, result);
  }

  @Test
  void givenHttpServletResponse_whenResolveArgument_thenReturnHttpServletResponse(@Mock HttpServletResponse response) throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", HttpServletResponse.class).getParameters()[0];

    var result = instance.resolveArgument(null, parameter, null, response);
    assertEquals(response, result);
  }

  @Test
  void givenHttpSession_whenResolveArgument_thenReturnHttpSession(@Mock HttpServletRequest request, @Mock HttpSession session) throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", HttpSession.class).getParameters()[0];

    when(request.getSession()).thenReturn(session);

    var result = instance.resolveArgument(null, parameter, request, null);
    assertEquals(session, result);

    verify(request).getSession();
  }

  @Test
  void givenServletContext_whenResolveArgument_thenReturnServletContext(
    @Mock HttpServletRequest request,
    @Mock ServletContext context
  ) throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", ServletContext.class).getParameters()[0];

    when(request.getServletContext()).thenReturn(context);

    var result = instance.resolveArgument(null, parameter, request, null);
    assertEquals(context, result);

    verify(request).getServletContext();
  }

  @Test
  void givenCookieArray_whenResolveArgument_thenReturnCookieArray(@Mock HttpServletRequest request) throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", Cookie[].class).getParameters()[0];
    Cookie[] expected = { new Cookie("value", "testing") };

    when(request.getCookies()).thenReturn(expected);

    var result = instance.resolveArgument(null, parameter, request, null);
    assertEquals(expected, result);

    verify(request).getCookies();
  }

  @Test
  void givenCookieList_whenResolveArgument_thenReturnCookieCollection(@Mock HttpServletRequest request) throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", List.class).getParameters()[0];
    List<Cookie> expected = List.of(new Cookie("value", "testing"));

    when(request.getCookies()).thenReturn(expected.toArray(new Cookie[1]));

    var result = instance.resolveArgument(null, parameter, request, null);
    assertEquals(expected, result);

    verify(request).getCookies();
  }

  void mockMethod(Set<Cookie> cookies) {}

  @Test
  void givenCookieSet_whenResolveArgument_thenReturnCookieCollection(@Mock HttpServletRequest request) throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", Set.class).getParameters()[0];
    Set<Cookie> expected = Set.of(new Cookie("value", "testing"));

    when(request.getCookies()).thenReturn(expected.toArray(new Cookie[1]));

    var result = instance.resolveArgument(null, parameter, request, null);
    assertEquals(expected, result);

    verify(request).getCookies();
  }

  @Test
  void givenCookieArrayTypeAndNoCookiePresent_whenResolveArgument_thenReturnEmptyArray(@Mock HttpServletRequest request) throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", Cookie[].class).getParameters()[0];

    when(request.getCookies()).thenReturn(null);

    var result = instance.resolveArgument(null, parameter, request, null);
    assertTrue(Cookie[].class.isAssignableFrom(result.getClass()));
    assertEquals(0, ((Cookie[]) result).length);

    verify(request).getCookies();
  }

  @Test
  void givenCookieCollectionTypeAndNoCookiePresent_whenResolveArgument_thenReturnEmptyCollection(@Mock HttpServletRequest request) throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethod", List.class).getParameters()[0];

    when(request.getCookies()).thenReturn(null);

    var result = instance.resolveArgument(null, parameter, request, null);
    assertTrue(List.class.isAssignableFrom(result.getClass()));
    assertTrue(((List<?>) result).isEmpty());

    verify(request).getCookies();
  }

  void mockMethodUnsupportedCookieCollectionType(Queue<Cookie> queue) {}

  @Test
  void givenCookieParameterAndUnsupportedCollectionType_whenResolveArgument_thenThrowUnsupportedArgumentException(@Mock HttpServletRequest request) throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethodUnsupportedCookieCollectionType", Queue.class).getParameters()[0];
    Cookie[] cookies = { new Cookie("value", "testing") };
    var expected = "Unsupported Cookie parameter type: %s. Consider using List<Cookie>, or Cookie[] as a method argument".formatted(parameter.getType());

    when(request.getCookies()).thenReturn(cookies);

    var result = assertThrows(UnsupportedArgumentException.class, () -> instance.resolveArgument(null, parameter, request, null));
    assertEquals(expected, result.getMessage());

    verify(request).getCookies();
  }

  @Test
  void givenUnsupportedParameterType_whenResolveArgument_thenThrowUnsupportedArgumentException() throws NoSuchMethodException {
    var parameter = getClass().getDeclaredMethod("mockMethodUnsupportedCollection", List.class).getParameters()[0];
    var expected = String.format(
      "Unsupported parameter type: %s. The supported parameter types are HttpServletRequest, HttpServletResponse, HttpSession, ServletContext, List<Cookie>",
      parameter.getType()
    );

    var result = assertThrows(UnsupportedArgumentException.class, () -> instance.resolveArgument(null, parameter, null, null));
    assertEquals(expected, result.getMessage());
  }

}