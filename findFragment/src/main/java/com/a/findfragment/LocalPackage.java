package com.a.findfragment;


import android.text.TextUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * 包处理相关类
 */
public class LocalPackage {

    /**
     * 获取指定包下面的所有Class
     *
     * @param packageName 包名
     * @return Class列表
     */
    public static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        // 第一个class类的集合
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (TextUtils.isEmpty(packageName)) return classes;
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageCodePath = packageName;
        DexFile df = new DexFile(packageCodePath);
        // 从此jar包 得到一个枚举类
        Enumeration<String> entries = df.entries();
        // 同样的进行循环迭代
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            String entry = entries.nextElement();
            String name = entry;
            if (name.indexOf("Fragment") < 0) continue;
            try {
                classes.add(Class.forName(name));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoClassDefFoundError e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    /**
     * 获取文件中的Class，并添加到集合
     *
     * @param packageName 包名
     * @param packagePath 包路径
     * @param recursive   是否循环迭代
     * @param classes     Class集合
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes) throws ClassNotFoundException {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] files = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : files) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "."
                                + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                // 添加到集合中去
                classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
            }
        }
    }

    /**
     * 获取有指定注解的Class
     *
     * @param annotation 注解类
     * @param classes    Class集合
     * @return 有注解的Class列表
     */
    public static List<Class<?>> getAnnotationClasses(Class annotation, List<Class<?>> classes) {
        List<Class<?>> annotationClasses = new ArrayList<Class<?>>();
        for (Class<?> clazz : classes) {
            if (clazz.getAnnotation(annotation) != null) {
                annotationClasses.add(clazz);
            }
        }
        return annotationClasses;
    }

    /**
     * 获取有指定注解的Class
     *
     * @param annotation  注解类
     * @param packageName 包名
     * @return 有注解的Class列表
     */
    public static List<Class<?>> getAnnotationClasses(Class annotation, String packageName) throws IOException, ClassNotFoundException {
        return getAnnotationClasses(annotation, getClasses(packageName));
    }

    /**
     * 获取指定注解的方法
     *
     * @param annotation 注解类
     * @param clazz      类
     * @return 使用注解的方法
     */
    public static List<Method> getAnnotationMethods(Class annotation, Class<?> clazz) {
        List<Method> annotationMethods = new ArrayList<Method>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getAnnotation(annotation) != null) {
                annotationMethods.add(method);
            }
        }
        return annotationMethods;
    }
}