package com.xiaoyong.helper;

import com.xiaoyong.annotation.Controller;
import com.xiaoyong.annotation.Repository;
import com.xiaoyong.annotation.Service;
import com.xiaoyong.utils.ClassUtil;
import com.xiaoyong.utils.PropertiesUtil;
import com.xiaoyong.utils.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author : XiaoYong
 * @date : 2018/5/8 21:03
 * Description    :
 */
public class BeanHelper {

    private static Map<Class<?>, Object> BEAN_CONTAINER = new HashMap<>();

    static {
//        获取classSet
        Set<Class<?>> classSet = ClassUtil.getClassSet(PropertiesHelper.getBasePackage());
//        遍历
        for (Class<?> clazz : classSet) {
//            如果被自定义的三个注解注解
            if (clazz.isAnnotationPresent(Controller.class)
                    || clazz.isAnnotationPresent(Service.class)
                    || clazz.isAnnotationPresent(Repository.class)) {
                Object bean = ReflectionUtil.newInstance(clazz);
//                向BEAN_CONTAINER中添加映射
                BEAN_CONTAINER.put(clazz, bean);
            }
        }
    }

    public static Map<Class<?>, Object> getBeanContainer() {
        return BEAN_CONTAINER;
    }

    public static <T> T getBean(Class<T> clazz) {
//        如果映射集中不包含clazz
        if (!BEAN_CONTAINER.containsKey(clazz)) {
//            抛出无法找到对应bean的异常，因为连与clazz对应key都没找到
            throw new RuntimeException("Could not found bean of " + clazz);
        }
        return (T)BEAN_CONTAINER.get(clazz);
    }

}
