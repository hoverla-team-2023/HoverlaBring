package org.bobocode.hoverla.bring.web.demo;

import org.bobocode.hoverla.bring.context.BeanDefinitionScanner;
import org.bobocode.hoverla.bring.context.BeanDefinitionScannerImpl;
import org.bobocode.hoverla.bring.context.DefaultBeanDefinitionRegistry;

public class Main {

    public static void main(String[] args) {
        BeanDefinitionScanner scanner = new BeanDefinitionScannerImpl();
        scanner.registerBeanDefinitions(new DefaultBeanDefinitionRegistry());
        var beanDefinitions = scanner.loadBeanDefinitions("org.bobocode.hoverla.bring");
        System.out.println("Found components: ");
        beanDefinitions.forEach(System.out::println);
    }

}