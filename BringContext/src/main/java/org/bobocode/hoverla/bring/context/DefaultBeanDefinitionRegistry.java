package org.bobocode.hoverla.bring.context;

import org.bobocode.hoverla.bring.bean.BeanDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {

    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitions.get(beanName);
    }

}
