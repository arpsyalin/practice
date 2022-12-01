package com.lyl.learn.debug;

import android.app.Application;

import com.lyl.sadness.Sadness;

public class DebugApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Sadness.getInstance().init(this);
        Sadness.getInstance().setGary(true);
    }
}
