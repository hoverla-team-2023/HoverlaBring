package org.bobocode.hoverla.bring.utils;

import lombok.experimental.UtilityClass;

/**
 * Utility class for handling Bean operations
 */
@UtilityClass
public class BeanUtils {

    /**
     * Generates a name for a bean class.
     *
     * @param beanClass The class of the bean.
     * @return The name of the class, used as the bean name.
     */
    public static String generateBeanName(Class<?> beanClass) {
        return beanClass.getSimpleName();
    }
}