package com.lyl.practice;

import android.app.Application;

import com.google.gson.Gson;
import com.lyl.arouter.ARouter;
import com.lyl.ifw.DealFactory;
import com.lyl.ifw.ICache;
import com.lyl.ifw.IJsonAnalysis;
import com.lyl.ifw.RequestMapBuilder;
import com.lyl.network.OkHttpNetwork;

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
        DealFactory.getInstance().network(new OkHttpNetwork()).jsonAnalysis(new IJsonAnalysis() {
            @Override
            public <T> T toJson(String data, Class<T> clazz) {
                Gson gson = new Gson();
                return gson.fromJson(data, clazz);
            }
        }).baseUrl(BuildConfig.SERVER_URL);
    }
}
