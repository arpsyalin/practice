package com.lyl.pluginmanage;

import android.os.Build;

/**
 * * @Description 版本判断工具
 * * @Author 刘亚林
 * * @CreateDate 2020/11/26
 * * @Version 1.0
 * * @Remark TODO
 **/
public class VersionUtils {

    public static boolean isAndroid10_11() {
        int mV = Build.VERSION.SDK_INT;
        return mV >= 29;
    }

    public static boolean isAndroid8_9() {
        int mV = Build.VERSION.SDK_INT;
        return mV >= 26 && mV <= 28;
    }

    public static boolean isAndroid5_7() {
        int mV = Build.VERSION.SDK_INT;
        return mV >= 21 && mV <= 25;
    }
}
