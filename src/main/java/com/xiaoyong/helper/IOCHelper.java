package com.xiaoyong.helper;

import com.xiaoyong.annotation.Autowired;
import com.xiaoyong.utils.ReflectionUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author : XiaoYong
 * @date : 2018/5/10 21:27
 * Description    :
 */
public class IOCHelper {

    static {
//        获取bean映射集
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanContainer();
//        如果不为空
        if (!beanMap.isEmpty()) {
//            遍历map
            for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
//                获取当前class
                Class<?> currentBeanClass = entry.getKey();
//                获取class对应的实例
                Object currentBeanInstance = entry.getValue();
//                遍历class中的变量
                Field[] fields = currentBeanClass.getDeclaredFields();
//                如果class中声明了变量
                if (!ArrayUtils.isEmpty(fields)) {
                    for (Field field : fields) {
//                        如果变量被Autowaird修饰
                        if (field.isAnnotationPresent(Autowired.class)) {
//                            获取依赖的class
                            Class<?> dependClass = field.getType();
//                            获取class的实例
                            Object dependInstance = beanMap.get(dependClass);
//                            如果存在实例
                            if (dependInstance != null) {
//                                注入
                                ReflectionUtil.setField(currentBeanInstance, field, dependInstance);
                            } else {
//                                如果不存在，抛出错误，不存在的原因可能是
//                                 要注入的bean没有使用Service，Controller等注解修饰
                                throw new RuntimeException("bean of " + dependClass + " not found");
                            }
                        }
                    }
                }
            }
        }
    }
}
