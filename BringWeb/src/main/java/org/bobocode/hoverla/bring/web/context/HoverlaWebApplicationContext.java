package org.bobocode.hoverla.bring.web.context;

import org.bobocode.hoverla.bring.context.HoverlaApplicationContext;

public class HoverlaWebApplicationContext extends HoverlaApplicationContext {

  // todo private
  /**
   * Constructor by default that will invoke all the magic under the hood.
   * It will start to search classes marked with @Component annotation and create beans from them
   *
   * @param path this is a path to folder where beanDefinitionScanner will start search.
   *             Please note if you will add as path /com/asd/qwe but you have next class SomeService.class in folder /com/asd/qwe/service it will be also find by scanner
   */

  public HoverlaWebApplicationContext(String path) {
    super(path); //todo  need tu update parent constructor to call doProcessBeans(); here after register BringWeb postProcessors
    /// TODO: register created BeanPostProcessors f.e. HandlerMappingProcessor, ExceptionHandlerBpp
    //todo also we can register servlet context here
//    getBeanFactory().addBeanPostProcessor();
  }

}
