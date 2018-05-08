package com.xiaoyong.helper;

import com.xiaoyong.annotation.Controller;
import com.xiaoyong.annotation.Repository;
import com.xiaoyong.annotation.Service;
import com.xiaoyong.utils.ClassUtil;
import com.xiaoyong.utils.PropertiesUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author : XiaoYong
 * @date : 2018/5/8 21:03
 * Description    :
 */
public class ClassHelper {

    public static Map<Class<?>, Object> BEAN_CONTAINER = new HashMap<>();

    static {
//        获取classSet
        Set<Class<?>> classSet = ClassUtil.getClassSet(PropertiesHelper.getBasePackage());
//        遍历
        for (Class<?> clazz : classSet) {
//            如果被自定义的三个注解注解
            if (clazz.isAnnotationPresent(Controller.class)
                    || clazz.isAnnotationPresent(Service.class)
                    || clazz.isAnnotationPresent(Repository.class)) {
                Object bean = null;
                try {
//                    实例化clazz
                    bean = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
//                向BEAN_CONTAINER中添加映射
                BEAN_CONTAINER.put(clazz, bean);
            }
        }

    }

}
