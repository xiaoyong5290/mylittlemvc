package com.xiaoyong.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author : XiaoYong
 * @date : 2018/5/9 9:19
 * Description    :
 */
public class ReflectionUtil {

    public static Object newInstance(Class<?> clazz) {
        Object instance = null;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    public static Object invokeMethod(Object object, Method method, Object... args) {
        Object result = null;
        try {
//            将方法设置为可访问
            method.setAccessible(true);
//            调用方法，取得方法的运行结果
            result = method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void setField(Object object, Field field, Object value) {
//        设置字段为可访问
        field.setAccessible(true);
        try {
//            设置字段的值
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
