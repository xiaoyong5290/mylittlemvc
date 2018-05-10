package com.xiaoyong.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author : XiaoYong
 * @date : 2018/5/8 21:02
 * Description    :
 */
public class PropertiesUtil {
    public static Properties getProperties(String propertiesFileName) {
        Properties properties = new Properties();
        InputStream in = ClassUtil.getClassLoader().getResourceAsStream(propertiesFileName);
        try {
            properties.load(new InputStreamReader(in, "utf-8"));
        } catch (IOException e) {
            throw new RuntimeException("properties file load error");
        }
        return properties;
    }

    public static String getString(Properties properties, String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static String getString(Properties properties, String key) {
        return properties.getProperty(key, null);
    }
}
