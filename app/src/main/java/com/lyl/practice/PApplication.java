package com.lyl.practice;

import android.app.Application;

import com.lyl.arouter.ARouter;

/**
 * * @Description Application
 * * @Author 刘亚林
 * * @CreateDate 2020/10/6
 * * @Version 1.0
 * * @Remark TODO
 **/
public class PApplication extends Application {
    static PApplication instance;

    public static PApplication getInstant() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ARouter.getInstance().init(this);
    }
}
