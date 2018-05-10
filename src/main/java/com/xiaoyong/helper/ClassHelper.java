package com.xiaoyong.helper;

import com.xiaoyong.annotation.Controller;
import com.xiaoyong.annotation.Service;
import com.xiaoyong.common.ConfigConstant;
import com.xiaoyong.utils.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : XiaoYong
 * @date : 2018/5/9 8:59
 * Description    :
 */
public class ClassHelper {
    private static final Set<Class<?>> CLASS_SET;
    private static Set<Class<?>> serviceClassSet;
    private static Set<Class<?>> controllerClassSet;
    private static Set<Class<?>> beanClassSet;

    static {
//        从ConfigConstant获取要扫描的包名
        String basePackage = ConfigConstant.BASE_PACKAGE_KEY;
//        使用ClassUtil的getClassSet方法得到CLASS_SET
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    public static Set<Class<?>> getServiceClassSet() {
//        如果serviceClassSet等于null，表示没有获取过，在if块中获取
        if (serviceClassSet == null) {
            serviceClassSet = new HashSet<>();
        }
        return getSpecifiedClassSet(serviceClassSet, Service.class);
    }

    public static Set<Class<?>> getControllerClassSet() {
//        如果controllerClassSet等于null，表示没有获取过，在if块中获取
        if (controllerClassSet == null) {
            controllerClassSet = new HashSet<>();
        }
        return getSpecifiedClassSet(controllerClassSet, Controller.class);
    }

    public static Set<Class<?>> getBeanClassSet() {
//        如果beanClassSet等于null，表示没有获取过，在if块中获取
        if (beanClassSet == null) {
            beanClassSet = new HashSet<>();
//            将被Controller和Service修饰的类都加入BEAN_CLASS_SET
            beanClassSet.addAll(getControllerClassSet());
            beanClassSet.addAll(getServiceClassSet());
        }
        return beanClassSet;
    }

    public static Set<Class<?>> getSpecifiedClassSet(Set<Class<?>> classSet, Class<? extends Annotation> annotationClass) {
//            遍历CLASS_SET中被注解修饰的类
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                classSet.add(clazz);
            }
        }
        return classSet;
    }
}
