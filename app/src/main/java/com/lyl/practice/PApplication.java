package com.lyl.practice;

import android.app.Application;

import com.lyl.arouter.ARouter;
import com.lyl.ifw.netexample.DealFactory;
import com.lyl.ifw.netframeapi.IDataAnalysis;
import com.lyl.ifw.netexample.RequestMapBuilder;
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
        DealFactory.getInstance().network(new OkHttpNetwork()).defaultAnalysis(new IDataAnalysis() {

            @Override
            public Object analysis(RequestMapBuilder requestMapBuilder, Object data) {
                return data;
            }
        }).baseUrl(BuildConfig.SERVER_URL);
    }
}
