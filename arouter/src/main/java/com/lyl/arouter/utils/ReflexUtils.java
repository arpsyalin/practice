package com.lyl.arouter.utils;

import java.lang.reflect.Constructor;

/**
 * * @Description 反射工具
 * * @Author 刘亚林
 * * @CreateDate 2020/10/6
 * * @Version 1.0
 * * @Remark TODO
 **/
public class ReflexUtils {
    public static void genObjByClassName(String className) {
        try {
            Class clazz = Class.forName(className);
            Object o = clazz.newInstance();
            o = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
