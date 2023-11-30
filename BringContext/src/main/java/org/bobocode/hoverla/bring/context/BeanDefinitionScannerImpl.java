package org.bobocode.hoverla.bring.context;

import lombok.extern.slf4j.Slf4j;
import org.bobocode.hoverla.bring.annotations.Scope;
import org.bobocode.hoverla.bring.bean.BeanDefinition;
import org.bobocode.hoverla.bring.bean.BeanScope;
import org.bobocode.hoverla.bring.utils.BeanUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.bobocode.hoverla.bring.utils.PathUtils.getClassName;
import static org.bobocode.hoverla.bring.utils.PathUtils.getFiles;
import static org.bobocode.hoverla.bring.utils.PathUtils.getPackageName;
import static org.bobocode.hoverla.bring.utils.PathUtils.isClass;
import static org.bobocode.hoverla.bring.utils.PathUtils.toFileSystemPath;

@Slf4j
public class BeanDefinitionScannerImpl implements BeanDefinitionScanner {

  private final BeanDefinitionRegistry registry;

  private final Set<Class<? extends Annotation>> annotations;

  /**
   * Constructs a BeanDefinitionScanner with specified registry and annotations.
   *
   * @param registry    The registry for bean definitions.
   * @param annotations Set of annotations to identify beans for scanning and registration.
   */
  public BeanDefinitionScannerImpl(BeanDefinitionRegistry registry,
                                   Set<Class<? extends Annotation>> annotations) {
    this.registry = registry;
    this.annotations = annotations;
  }

  /**
   * Loads bean definitions from a specified base package. This method scans the given package
   * for classes annotated as components, creates bean definitions for them, and registers them
   * in the bean definition registry.
   *
   * @param basePackage The base package to scan for bean classes.
   *
   * @return A list of {@link BeanDefinition} objects representing the beans found in the package.
   */
  @Override
  public List<BeanDefinition> loadBeanDefinitions(String basePackage) {
    List<BeanDefinition> beanDefinitions = new ArrayList<>();
    Set<Class<?>> beanClasses = this.findCandidatesComponents(basePackage);

    for (Class<?> beanClass : beanClasses) {
      var beanName = BeanUtils.generateBeanName(beanClass);
      log.debug("Registering bean: " + beanName);
      var beanDefinition = getBeanDefinition(beanClass, beanName);
      this.registry.registerBeanDefinition(beanName, beanDefinition);
      log.debug("Bean with name " + beanName + "registered successfully");
      beanDefinitions.add(beanDefinition);
    }

    return beanDefinitions;
  }

  private Set<Class<?>> findCandidatesComponents(String basePackage) {
    Set<Class<?>> classes = new HashSet<>();
    var resources = getResources(toFileSystemPath(basePackage));
    while (resources.hasMoreElements()) {
      var resource = resources.nextElement();
      try {
        var directory = new File(resource.toURI());
        var result = findClasses(directory, basePackage);
        classes.addAll(result);
      } catch (Exception ignored) {
      }
    }
    log.info("Scanning for components completed");
    return classes;
  }

  private Set<Class<?>> findClasses(File directory, String parentPackageName) {

    Set<Class<?>> classes = new HashSet<>();
    if (directory == null || !directory.exists()) {
      log.debug("Directory not found: " + directory);
      return classes;
    }

    File[] files = getFiles(directory);
    for (File file : files) {
      if (file.isDirectory()) {
        String packageName = getPackageName(parentPackageName, file.getName());
        log.debug("Scanning directory: " + packageName);
        classes.addAll(findClasses(file, packageName));
      } else if (isClass(file.getName())) {
        String className = getClassName(parentPackageName, file.getName());
        log.debug("Found class: " + className);
        Class<?> clazz = getClassByName(className);
        if (clazz != null && isAnnotationPresent(clazz)) {
          log.debug("Class annotated with " + Arrays.toString(clazz.getAnnotations()) + ": " + className);
          classes.add(clazz);
        }
      }
    }
    return classes;
  }

  private static Enumeration<URL> getResources(String filePath) {
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      return classLoader.getResources(filePath);
    } catch (IOException e) {
      log.error("Failed to scan classes with path: " + e);
    }
    return Collections.emptyEnumeration();
  }

  private Class<?> getClassByName(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException | NoClassDefFoundError e) {
      log.error("Failed to load class by class name: " + e);
    }
    return null;
  }

  private boolean isAnnotationPresent(Class<?> clazz) {
    for (Class<? extends Annotation> targetAnnotation : annotations) {
      Annotation[] annotationsOnClass = clazz.getAnnotations();
      for (Annotation annotation : annotationsOnClass) {
        if (clazz.isAnnotationPresent(targetAnnotation)
                || annotation.annotationType().isAnnotationPresent(targetAnnotation)) {
          return true;
        }
      }
    }
    return false;
  }

  private static BeanDefinition getBeanDefinition(Class<?> beanClass, String beanName) {
    BeanScope scope = Optional.ofNullable(beanClass.getAnnotation(Scope.class))
      .map(Scope::value)
      .orElse(BeanScope.SINGLETON);
    return BeanDefinition.of(beanName, beanClass, scope);
  }

}
