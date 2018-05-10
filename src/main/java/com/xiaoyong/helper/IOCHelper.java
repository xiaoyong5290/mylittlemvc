package com.xiaoyong.helper;

import com.xiaoyong.annotation.Autowaird;
import com.xiaoyong.utils.ReflectionUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * @author : XiaoYong
 * @date : 2018/5/10 21:27
 * Description    :
 */
public class IOCHelper {

    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanContainer();
        if (!beanMap.isEmpty()) {
            for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
                Class<?> currentBeanClass = entry.getKey();
                Object currentBeanInstance = entry.getValue();
                Field[] fields = currentBeanClass.getDeclaredFields();
                if (!ArrayUtils.isEmpty(fields)) {
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Autowaird.class)) {
                            Class<?> dependClass = field.getType();
                            Object dependInstance = beanMap.get(dependClass);
                            if (dependInstance != null) {
                                ReflectionUtil.setField(currentBeanInstance, field, dependInstance);
                            } else {
                                throw new RuntimeException("bean of " + dependClass + " not found");
                            }
                        }
                    }
                }
            }
        }
    }
}
