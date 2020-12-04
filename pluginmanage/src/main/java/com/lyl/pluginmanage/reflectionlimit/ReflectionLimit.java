package com.lyl.pluginmanage.reflectionlimit;

import com.lyl.pluginmanage.VersionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * * @Description 9.0反射限制问题
 * * @Author 刘亚林
 * * @CreateDate 2020/11/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public final class ReflectionLimit {
    //https://editor.csdn.net/md/?articleId=110383018
    private static Object sVMRuntime;
    private static Method setHiddenApiExemptions;

    static {
        if (VersionUtils.isAndroid10_11()) {
            try {
                Method forName = Class.class.getDeclaredMethod("forName", String.class);
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
                Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
                Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
                setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
                setHiddenApiExemptions.setAccessible(true);
                sVMRuntime = getRuntime.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //消除限制
    public static boolean clearLimit() {
        if (sVMRuntime == null || setHiddenApiExemptions == null) {
            return false;
        }
        try {
            setHiddenApiExemptions.invoke(sVMRuntime, new Object[]{new String[]{"L"}});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
