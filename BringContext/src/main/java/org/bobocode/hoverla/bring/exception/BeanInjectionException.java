package org.bobocode.hoverla.bring.exception;

/**
 * This exception using to detect problem with injection of bean
 * common reason:
 *  * No bean definition found(it can happen if you set incorrect scanning path, or did not mark class you want to inject as @Component or similar annotation),
 * `* More then 1 bean to inject found(it can happen when you created interface f.e. UserService and implemented it by SystemUserService and ExternalUserService,
 *      then marked both as @Component and try to autowire it by next way: @Autowired private UserService userService;
 */
public class BeanInjectionException extends RuntimeException {

  public BeanInjectionException(String message) {
    super(message);
  }

  public BeanInjectionException(String message, Throwable cause) {
    super(message, cause);
  }

}
