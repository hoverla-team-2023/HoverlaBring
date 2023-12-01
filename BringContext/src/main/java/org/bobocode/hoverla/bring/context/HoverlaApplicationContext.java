package org.bobocode.hoverla.bring.context;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import org.bobocode.hoverla.bring.annotations.Component;
import org.bobocode.hoverla.bring.factory.BeanFactory;
import org.bobocode.hoverla.bring.factory.BeanFactoryImpl;
import org.bobocode.hoverla.bring.processors.AutowireBeanPostProcessor;
import org.bobocode.hoverla.bring.processors.BeanFactoryPostProcessor;
import org.bobocode.hoverla.bring.processors.BeanPostProcessor;

import lombok.extern.slf4j.Slf4j;

/**
 * Main class of Bring Context application, responsible for manage of all main process in this app
 */
@Slf4j
public class HoverlaApplicationContext implements ApplicationContext {

  private BeanDefinitionScanner beanDefinitionScanner;
  private BeanFactory beanFactory;
  private BeanDefinitionRegistry beanDefinitionRegistry;

  Set<Class<? extends Annotation>> allowedBeanDefinedAnnotations = Set.of(Component.class);

  /**
   * Constructor by default that will invoke all the magic under the hood.
   * It will start to search classes marked with @Component annotation and create beans from them
   *
   * @param path this is a path to folder where beanDefinitionScanner will start search.
   *             Please note if you will add as path /com/asd/qwe but you have next class SomeService.class in folder /com/asd/qwe/service it will be also find by scanner
   */
  public HoverlaApplicationContext(String path) {
    log.info("Context initialization started, path to scan: {}", path);
    init();
    log.debug("Context initialization finished successfully, starting package scanning");
    beanDefinitionScanner.loadBeanDefinitions(path);
    beanFactory.getBeanFactoryPostProcessors().forEach(bfpp -> bfpp.postProcessBeanFactory(beanFactory));
    this.doProcessBeans();
    log.info("Scanning components finished successfully");
  }

  public HoverlaApplicationContext(String path, List<BeanFactoryPostProcessor> beanFactoryPostProcessors, List<BeanPostProcessor> beanPostProcessors) {
    log.info("Context initialization started, path to scan: {}", path);
    init();
    addCustomProcessors(beanFactoryPostProcessors, beanPostProcessors);
    log.debug("Context initialization finished successfully, starting package scanning");
    beanDefinitionScanner.loadBeanDefinitions(path);
    beanFactory.getBeanFactoryPostProcessors().forEach(bfpp -> bfpp.postProcessBeanFactory(beanFactory));
    this.doProcessBeans();
    log.info("Scanning components finished successfully");
  }

  private void addCustomProcessors(List<BeanFactoryPostProcessor> beanFactoryPostProcessors, List<BeanPostProcessor> beanPostProcessors) {
    if (beanFactoryPostProcessors != null) {
      beanFactoryPostProcessors.forEach(beanFactoryPostProcessor -> beanFactory.addBeanFactoryPostProcessor(beanFactoryPostProcessor));
    }
    if (beanPostProcessors != null) {
      beanPostProcessors.forEach(beanPostProcessor -> beanFactory.addBeanPostProcessor(beanPostProcessor));
    }
  }

  /**
   * Get the BeanFactory associated with this object.
   *
   * @return BeanFactory instance used by this object.
   */
  @Override
  public BeanFactory getBeanFactory() {
    return beanFactory;
  }

  @Override
  public Object getBean(String beanName) {
    return beanFactory.getBean(beanName);
  }

  @Override
  public Object getBean(Class<?> beanClass) {
    return beanFactory.getBean(beanClass);
  }

  /**
   * This method is responsible for initialization of required HoverlaApplicationContext params
   */
  private void init() {
    this.beanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
    this.beanDefinitionScanner = new BeanDefinitionScannerImpl(beanDefinitionRegistry, allowedBeanDefinedAnnotations);
    this.beanFactory = initBeanFactory();
  }

  /**
   * This method is responsible for initialization of BeanFactory and add defaults BeanPostProcessors
   */
  private BeanFactory initBeanFactory() {
    log.debug("BeanFactory initializing started");
    BeanFactoryImpl beanFactoryImpl = new BeanFactoryImpl();
    beanFactoryImpl.addBeanPostProcessor(new AutowireBeanPostProcessor(beanFactoryImpl));
    beanFactoryImpl.setBeanDefinitionRegistry(beanDefinitionRegistry);
    log.debug("Bean factory successfully initialized");
    return beanFactoryImpl;
  }

  protected void doProcessBeans() {
    for (String beanDefinitionName : beanDefinitionRegistry.getAllBeanDefinitionNames()) {
      log.debug("Processing bean with name {} started", beanDefinitionName);
      beanFactory.tryToInitializeSingletonBean(beanDefinitionName);
      log.debug("Processing bean with name {} finished", beanDefinitionName);
    }
  }
}
