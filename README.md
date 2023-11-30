# Hoverla-Bring
[![Coverage](http://ec2-3-65-176-142.eu-central-1.compute.amazonaws.com:9000/api/project_badges/measure?project=hoverla-team-2023_HoverlaBring_AYwIBAvijYd-lSknBhZO&metric=coverage&token=sqb_7e5c712a7a7d795a06fca18de48eddc5514e1a54)](http://ec2-3-65-176-142.eu-central-1.compute.amazonaws.com:9000/dashboard?id=hoverla-team-2023_HoverlaBring_AYwIBAvijYd-lSknBhZO)
[![Duplicated Lines (%)](http://ec2-3-65-176-142.eu-central-1.compute.amazonaws.com:9000/api/project_badges/measure?project=hoverla-team-2023_HoverlaBring_AYwIBAvijYd-lSknBhZO&metric=duplicated_lines_density&token=sqb_7e5c712a7a7d795a06fca18de48eddc5514e1a54)](http://ec2-3-65-176-142.eu-central-1.compute.amazonaws.com:9000/dashboard?id=hoverla-team-2023_HoverlaBring_AYwIBAvijYd-lSknBhZO)
[![Lines of Code](http://ec2-3-65-176-142.eu-central-1.compute.amazonaws.com:9000/api/project_badges/measure?project=hoverla-team-2023_HoverlaBring_AYwIBAvijYd-lSknBhZO&metric=ncloc&token=sqb_7e5c712a7a7d795a06fca18de48eddc5514e1a54)](http://ec2-3-65-176-142.eu-central-1.compute.amazonaws.com:9000/dashboard?id=hoverla-team-2023_HoverlaBring_AYwIBAvijYd-lSknBhZO)

# Dispatcher Servlet

To initialize a dispatcher servlet you need to introduce a new class in classpath, witch extends `AbstractDispatcherServletInitializer`.
This class will then be scanned and initialized automatically. Please note that the class should have a public no-args constructor.

Example:

```java
public class DispatcherServletConfiguration extends AbstractDispatcherServletInitializer {

  @Override
  protected String getServletMapping() {
    return "/";
  }

  @Override
  protected Object[] controllers() {
    return new Object[] { new HoverlaController() };
  }

}

```

To register a controller just mark your class with `@Controller` annotation. After that you are able to introduce your own endpoints 
by marking them with `@RequestMapping` annotation.

Example: 

```java
@Controller
public class MyBobocodeController {

  @RequestMapping(path = "/bobocode/hoverla", method = RequestMethod.GET)
  public TeamDTO helloWorld() {
    return new TeamDTO("hoverla", Map.of(
      "test1", "user1",
      "test2", "user2"
    ));
  }

}

record TeamDTO(String team, Map<String, String> teammates) {
}
```

##### Request/Response objects
The Bring Framework supports `application/json` and `text/plain` content types. Thus, all POJOs that are returned by the servlet
are converted to JSON, and all primitive values are returned as `text/plain`. Also, if you want to receive an object in endpoint, it should be sent in JSON format.
To mark an endpoint argument as the request body, annotate it with `@RequestBody` annotation. Also, you can use `RequestEntity<T>` argument
type instead. It will give you access to the request body and request headers.

Example:
```java
@RequestMapping(path = "/bobocode/hoverla-request-body-list", method = RequestMethod.POST) 
public void testRequestBodyDtoList(@RequestBody List<TestDto> body) {
  System.out.println(body);
}

@RequestMapping(path = "/bobocode/hoverla-request-entity", method = RequestMethod.POST)
public void testRequestEntity(RequestEntity<TestDto> requestEntity) {
  System.out.println(requestEntity);
  }
```

You can mark your method with `@StatusCode` annotation to provide status code for the response:
```java
@StatusCode(202)
@RequestMapping(path = "/bobocode/hoverla-dto", method = RequestMethod.GET)
public List<TeamDTO> helloWorldDto() {
  var dto = new TeamDTO("hoverla", Map.of(
    "test1", "user1",
    "test2", "user2"
  ));
  return List.of(dto);
}
```

`ResponseEntity<T>` return type gives an ability to specify response body, headers, and status code:
```java
@RequestMapping(path = "/bobocode/training", method = RequestMethod.GET)
public ResponseEntity<String> training() {
  return new ResponseEntity<>(
    "hoverla",                                     // response body
    Map.of("My-Header", List.of("testValue")),     // response headers
    201                                            // response status code
  );
}
```

Also, you can inject additional jakarta objects into your endpoint. Such as:
- request - `HttpServletRequest`
- response - `HttpServletResponse`
- session - `HttpSession` 
- servlet context - `ServletContext`
- cookies - `Cookie[]`, or `List<Cookie>`, or `Set<Cookie>`

Example:
```java
@RequestMapping(path = "/bobocode/hoverla-servlet-objects", method = RequestMethod.GET)
public void testServletObjects(HttpServletRequest request,
                               HttpServletResponse response,
                               HttpSession session,
                               ServletContext context,
                               Cookie[] cookiesArray,
                               List<Cookie> cookieList,
                               Set<Cookie> cookieSet) {

  session.setAttribute("hello", "world");
}
```