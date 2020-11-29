package com.lyl.pluginmanage;


import android.app.Application;

import com.lyl.pluginmanage.hookv.Android10_11Hook;
import com.lyl.pluginmanage.hookv.Android5_7Hook;
import com.lyl.pluginmanage.hookv.Android8_9Hook;

/**
 * * @Description 插件包管理封装
 * * @Author 刘亚林
 * * @CreateDate 2020/11/25
 * * @Version 1.0
 * * @Remark TODO
 **/
public class PluginPackageManager {
    public static volatile PluginPackageManager sPluginPackageManager;
    private Application application;
    IAndroidHook androidHook;

    private PluginPackageManager() {
    }

    public static PluginPackageManager getInstance() {
        if (sPluginPackageManager == null) {
            synchronized (PluginPackageManager.class) {
                if (sPluginPackageManager == null) {
                    sPluginPackageManager = new PluginPackageManager();
                }
            }
        }
        return sPluginPackageManager;
    }

    public void hook() throws Exception {
        if (application == null) throw new Exception("please setApplication");
        if (VersionUtils.isAndroid10_11()) {
            androidHook = new Android10_11Hook();
        }
        if (VersionUtils.isAndroid8_9()) {
            androidHook = new Android8_9Hook();
        }
        if (VersionUtils.isAndroid5_7()) {
            androidHook = new Android5_7Hook();
        }

        if (androidHook == null) {
            throw new Exception("not support system version");
        }
        androidHook.hookAms(application);
        androidHook.hookActivityThread(application);

    }

    public PluginPackageManager setApplication(Application application) {
        this.application = application;
        return this;
    }
}
