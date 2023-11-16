package org.bobocode.hoverla.bring.context;

import org.bobocode.hoverla.bring.annotations.Scope;
import org.bobocode.hoverla.bring.bean.BeanDefinition;

import org.bobocode.hoverla.bring.annotations.Component;
import org.bobocode.hoverla.bring.bean.BeanScope;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.logging.Logger;

import static org.bobocode.hoverla.bring.utils.PathUtils.*;

public class BeanDefinitionScannerImpl implements BeanDefinitionScanner {

    private static final Logger logger = Logger.getLogger(BeanDefinitionScannerImpl.class.getName());
    private final static BeanNameGenerator GENERATOR = new DefaultBeanNameGenerator();
    private BeanDefinitionRegistry registry;

    /**
     * This method sets the registry instance to the provided {@link BeanDefinitionRegistry}.
     * It is used for integrating the current bean processing logic with the specified registry.
     *
     * @param registry The {@link BeanDefinitionRegistry} where the bean definitions are to be registered.
     */
    @Override
    public void registerBeanDefinitions(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    /**
     * Loads bean definitions from a specified base package. This method scans the given package
     * for classes annotated as components, creates bean definitions for them, and registers them
     * in the bean definition registry.
     *
     * @param basePackage The base package to scan for bean classes.
     * @return A list of {@link BeanDefinition} objects representing the beans found in the package.
     */
    @Override
    public List<BeanDefinition> loadBeanDefinitions(String basePackage) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        Set<Class<?>> beanClasses = this.findCandidatesComponents(basePackage);

        for (Class<?> beanClass : beanClasses) {
            var beanName = GENERATOR.generateBeanName(beanClass);
            logger.info("Registering bean: " + beanName);
            var beanDefinition = getBeanDefinition(beanClass, beanName);
            this.registry.registerBeanDefinition(beanName, beanDefinition);
            beanDefinitions.add(beanDefinition);
        }

        return beanDefinitions;
    }

    private Set<Class<?>> findCandidatesComponents(String path) {
        Set<Class<?>> classes = new HashSet<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            var resources = classLoader.getResources(toFileSystemPath(path));
            while (resources.hasMoreElements()) {
                var resource = resources.nextElement();
                var directory = new File(resource.getFile());
                classes.addAll(findClasses(directory, toPackageNamePath(path), Component.class));
            }
        } catch (IOException e) {
            logger.severe("Failed to scan classes with path: " + e);
        }
        logger.info("Scanning for components completed");
        return classes;
    }

    private Set<Class<?>> findClasses(File directory, String parentPackageName,
                                      Class<? extends Annotation> annotation) {

        Set<Class<?>> classes = new HashSet<>();
        if (directory == null || !directory.exists()) {
            logger.warning("Directory not found: " + directory);
            return classes;
        }

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                String packageName = getPackageName(parentPackageName, file.getName());
                logger.fine("Scanning directory: " + packageName);
                classes.addAll(findClasses(file, packageName, annotation));
            } else if (isClass(file.getName())) {
                String className = getClassName(parentPackageName, file.getName());
                logger.fine("Found class: " + className);
                Class<?> clazz = getClassByName(className);
                if (clazz != null && clazz.isAnnotationPresent(annotation)) {
                    logger.fine("Class annotated with " + annotation.getName() + ": " + className);
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }

    private Class<?> getClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            logger.severe("Failed to load class by class name: " + e);
        }
        return null;
    }

    private static BeanDefinition getBeanDefinition(Class<?> beanClass, String beanName) {
        BeanScope scope = Optional.ofNullable(beanClass.getAnnotation(Scope.class))
                .map(Scope::value)
                .orElse(BeanScope.SINGLETON);
        return BeanDefinition.of(beanName, beanClass, scope);
    }
}
