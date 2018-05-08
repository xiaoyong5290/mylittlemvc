package com.xiaoyong.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author : XiaoYong
 * @date : 2018/5/8 21:03
 * Description    :
 */
public class ClassUtil {
    private static final Logger logger = Logger.getLogger(ClassUtil.class);

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className, boolean isInit) {
        Class<?> clazz;
        try {
//            加载clazz但不初始化
            clazz = Class.forName(className, isInit, getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return clazz;
    }


    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<>();

        try {
//            扫描指定包下的文件夹和文件
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
//                迭代
                URL url = urls.nextElement();
                if (url != null) {
//                    获取协议
                    String protocol = url.getProtocol();
//                    如果是文件
                    if ("file".equals(protocol)) {
                        //        以utf-8编码解码url
                        String packagePath = URLDecoder.decode(url.getPath(), "utf-8");
                        addClassFromFile(classSet, packagePath, packageName);
                    }
//                    如果是jar包
                    if ("jar".equals(protocol)) {
                        addClassFromJar(classSet, url);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classSet;
    }

    public static void addClassFromJar(Set<Class<?>> classSet, URL url) throws IOException {
//        通过JAR协议建立一个jar URL的连接
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
//        连接建立成功
        if (jarURLConnection != null) {
//            通过连接获取jarFile对象
            JarFile jarFile = jarURLConnection.getJarFile();
//            成功获取到jarFile对象，相当于jar包
            if (jarFile != null) {
//                获取jarEntries，相当于jar包中的目录和.class文件
                Enumeration<JarEntry> jarEntries = jarFile.entries();
//                如果jar中有文件或目录
                if (jarEntries.hasMoreElements()) {
//                    遍历jar包中的目录和文件
                    JarEntry jarEntry = jarEntries.nextElement();
//                    获取文件或目录的名称
                    String jarEntryName = jarEntry.getName();
//                    如果是class文件
                    if (jarEntryName.endsWith(".class")) {
//                        截取类名
                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                        addClassByClassName(classSet, className);
                    }
                }
            }
        }
    }

    public static void addClassFromFile(Set<Class<?>> classSet, String packagePath, String packageName) throws UnsupportedEncodingException {
//        将除了.class文件和目录外的其它文件过滤掉
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
//                如果是.class文件或目录，返回true
                return (pathname.isFile() && pathname.getName().endsWith(".class")) || pathname.isDirectory();
            }
        });
        for (File file : files) {
            String fileName = file.getName();
//            如果是文件
            if (file.isFile()) {
//                截取类名
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotEmpty(packageName)) {
//                    将类名组装成包名+类名的形式
                    className = packageName + "." + className;
                }
                addClassByClassName(classSet, className);
            }
//            如果是目录
            else {
                String subPackagePath = fileName;
                if (StringUtils.isNotEmpty(packagePath)) {
//                    拼装路径
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(packageName)) {
//                    拼装包名
                    subPackageName = packageName + "." + subPackageName;
                }
                addClassFromFile(classSet, subPackagePath, subPackageName);
            }
        }
    }

    public static void addClassByClassName(Set<Class<?>> classSet, String className) {
//        加载clazz
        Class<?> clazz = loadClass(className, false);
//        将clazz加入classSet
        classSet.add(clazz);
    }
}
