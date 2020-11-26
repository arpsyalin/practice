package com.lyl.pluginmanage;

import android.app.Application;

public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            PluginPackageManager.getInstance().setApplication(this).hook();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
