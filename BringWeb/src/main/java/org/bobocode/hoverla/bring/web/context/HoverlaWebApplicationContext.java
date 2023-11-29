package org.bobocode.hoverla.bring.web.context;

import org.bobocode.hoverla.bring.context.HoverlaApplicationContext;
import org.bobocode.hoverla.bring.factory.BeanFactory;
import org.bobocode.hoverla.bring.web.processors.HandlerMappingProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    super(path);
    //Register BeanPostProcessors add any other BeanPostProcessors if needed
    BeanFactory beanFactory = getBeanFactory();
    HandlerMappingProcessor postProcessor = new HandlerMappingProcessor(beanFactory);
    beanFactory.addBeanPostProcessor(postProcessor);
    super.doProcessBeans();
    log.info("Bean processing finished successfully, app is ready to use");
  }
}
