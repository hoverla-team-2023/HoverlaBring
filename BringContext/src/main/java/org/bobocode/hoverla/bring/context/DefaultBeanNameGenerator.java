package org.bobocode.hoverla.bring.context;

public class DefaultBeanNameGenerator implements BeanNameGenerator {
    @Override
    public String generateBeanName(Class<?> beanClass) {
        return beanClass.getSimpleName();
    }
}
