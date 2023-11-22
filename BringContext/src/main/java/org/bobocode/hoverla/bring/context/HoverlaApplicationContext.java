package org.bobocode.hoverla.bring.context;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.bobocode.hoverla.bring.annotations.Component;
import org.bobocode.hoverla.bring.factory.BeanFactory;
import org.bobocode.hoverla.bring.factory.BeanFactoryImpl;

/**
 * Main class of Bring Context application, responsible for manage of all main process in this app
 */
public class HoverlaApplicationContext implements ApplicationContext {

  private BeanDefinitionScanner beanDefinitionScanner;
  private BeanFactory beanFactory;
  private BeanDefinitionRegistry beanDefinitionRegistry;

  Set<Class<? extends Annotation>> allowedBeanDefinedAnnotations = new HashSet<>();

  /**
   * Constructor by default that will invoke all the magic under the hood.
   * It will start to search classes marked with @Component annotation and create beans from them
   *
   * @param path this is a path to folder where beanDefinitionScanner will start search.
   *             Please note if you will add as path /com/asd/qwe but you have next class SomeService.class in folder /com/asd/qwe/service it will be also find by scanner
   */
  public HoverlaApplicationContext(String path) {
    init();
    beanDefinitionScanner.loadBeanDefinitions(path);
    doProcessBeans();
  }

  /**
   * This method is responsible for initialization of required HoverlaApplicationContext params
   */
  private void init() {
    this.allowedBeanDefinedAnnotations.add(Component.class);
    this.beanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
    this.beanDefinitionScanner = new BeanDefinitionScannerImpl(beanDefinitionRegistry, allowedBeanDefinedAnnotations);
    this.beanFactory = initBeanFactory();
  }

  /**
   * This method is responsible for initialization of BeanFactory
   */
  private BeanFactory initBeanFactory() {
    BeanFactoryImpl beanFactoryImpl = new BeanFactoryImpl();
    //    beanFactoryImpl.addBeanPostProcessor(); //todo add default bpp(autowiringBpp) here the same about beanFactoryPostProcessors
    beanFactoryImpl.setBeanDefinitionRegistry(beanDefinitionRegistry);
    return beanFactoryImpl;
  }

  private void doProcessBeans() {
    for (String beanDefinitionName : beanDefinitionRegistry.getAllBeanDefinitionNames()) {
      beanFactory.tryToInitializeSingletonBean(beanDefinitionName);
    }
  }

  @Override
  public BeanFactory getBeanFactory() {
    return beanFactory;
  }

}
