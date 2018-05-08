package com.xiaoyong.helper;

import com.xiaoyong.utils.PropertiesUtil;

import java.util.Properties;

/**
 * @author : XiaoYong
 * @date : 2018/5/8 21:02
 * Description    :
 */
public class PropertiesHelper {
    private static Properties properties = PropertiesUtil.getProperties("config.properties");

    public static String getBasePackage() {
        return PropertiesUtil.getString(properties, "basePackage");
    }
}
