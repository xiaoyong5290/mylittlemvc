package com.xiaoyong.helper;

import com.xiaoyong.annotation.Controller;
import com.xiaoyong.annotation.Service;
import com.xiaoyong.common.ConfigConstant;
import com.xiaoyong.utils.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : XiaoYong
 * @date : 2018/5/9 8:59
 * Description    :
 */
public class ClassHelper {
    private static Set<Class<?>> CLASS_SET;
    private static Set<Class<?>> SERVICE_CLASS_SET;
    private static Set<Class<?>> CONTROLLER_CLASS_SET;
    private static Set<Class<?>> BEAN_CLASS_SET;

    static {
//        从ConfigConstant获取BASE_PACKAGE_KEY
        String basePackage = ConfigConstant.BASE_PACKAGE_KEY;
//        使用ClassUtil的getClassSet(String packageName)方法得到CLASS_SET
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    public static Set<Class<?>> getServiceClassSet() {
//        如果SERVICE_CLASS_SET等于null，表示没有获取过，在if块中获取
        if (SERVICE_CLASS_SET == null) {
            SERVICE_CLASS_SET = new HashSet<>();
//            遍历CLASS_SET中被Service注解修饰的类
            for (Class<?> clazz : CLASS_SET) {
                if (clazz.isAnnotationPresent(Service.class)) {
                    SERVICE_CLASS_SET.add(clazz);
                }
            }
        }
        return SERVICE_CLASS_SET;
    }

    public static Set<Class<?>> getControllerClassSet() {
//        如果CONTROLLER_CLASS_SET等于null，表示没有获取过，在if块中获取
        if (CONTROLLER_CLASS_SET == null) {
            CONTROLLER_CLASS_SET = new HashSet<>();
//            遍历CLASS_SET中被Controller注解修饰的类
            for (Class<?> clazz : CLASS_SET) {
                if (clazz.isAnnotationPresent(Controller.class)) {
                    CONTROLLER_CLASS_SET.add(clazz);
                }
            }
        }
        return CONTROLLER_CLASS_SET;
    }

    public static Set<Class<?>> getBeanClassSet() {
//        如果BEAN_CLASS_SET等于null，表示没有获取过，在if块中获取
        if (BEAN_CLASS_SET == null) {
            BEAN_CLASS_SET = new HashSet<>();
//            将被Controller和Service修饰的类都加入BEAN_CLASS_SET
            BEAN_CLASS_SET.addAll(getControllerClassSet());
            BEAN_CLASS_SET.addAll(getServiceClassSet());
        }
        return BEAN_CLASS_SET;
    }
}
