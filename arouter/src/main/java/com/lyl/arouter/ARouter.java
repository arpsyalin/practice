package com.lyl.arouter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.lyl.arouter.annotations.IInterceptor;
import com.lyl.arouter.constant.AConstant;
import com.lyl.arouter.utils.ClassUtils;
import com.lyl.arouter.utils.ReflexUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lyl.arouter.constant.AConstant.SCAN_ROUTER_CLASS;

/**
 * * @Description 路由收集器
 * * @Author 刘亚林
 * * @CreateDate 2020/9/29
 * * @Version 1.0
 * * @Remark TODO
 **/
public class ARouter {
    private final static String TAG = ARouter.class.getCanonicalName();
    private static ARouter instance = new ARouter();
    //保存路由跳转数据
    private Map<String, Class<? extends Activity>> mRouterData = new HashMap<>();
    private Map<String, Class<? extends IInterceptor>> mInterceptorData = new HashMap<>();
    //是否初始化
    private boolean hasInit = false;
//    public List<String> mRouterClassName = new ArrayList<>();

    public static ARouter getInstance() {
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public boolean init(Application context) {
        long time = System.currentTimeMillis();
        try {
            List<String> classArr = ClassUtils.getFileNameByPackageName(context, AConstant.SCAN_ROUTER_PACKAGE);
            time = (System.currentTimeMillis() - time);
            Log.e(TAG, "time:" + time);
            time = System.currentTimeMillis();
            for (String clazz : classArr) {
                if (clazz.contains(AConstant.SCAN_ROUTER_CLASS)) {
//                    mRouterClassName.add(clazz);
                    Log.e(TAG, "clazz:" + clazz);
                    ReflexUtils.genObjByClassName(clazz);
                }
            }
            hasInit = true;
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        time = (System.currentTimeMillis() - time);
        Log.e(TAG, "time:" + time);
        return false;
    }

    //添加路由数据
    public void addRouterData(String key, Class<? extends Activity> clazz) {
        Log.e(TAG, "addRouterData:" + key);
        mRouterData.put(key, clazz);
    }

    //添加拦截器
    public void addInterceptor(String key, Class<? extends IInterceptor> clazz) {
        Log.e(TAG, "addInterceptor:" + key);
        mInterceptorData.put(key, clazz);
    }

    //跳转Activity
    public void jumpActivity(Context context, String key) {
        jumpActivity(context, key, null);
    }

    public void jumpActivity(Context context, String key, Bundle bundle) {
        if (hasInit) {
            boolean isCanJump = isCanJump(mInterceptorData, key);
            if (!isCanJump && !TextUtils.isEmpty(key) && mRouterData.containsKey(key)) {
                Class<? extends Activity> clazz = mRouterData.get(key);
                if (clazz != null) {
                    Intent intent = new Intent();
                    intent.setClass(context, clazz);
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        } else {
            Log.e(TAG, "jumpActivity:" + key);
            if (init((Application) context.getApplicationContext())) {
                jumpActivity(context, key, bundle);
            }
        }
    }

    private boolean isCanJump(Map<String, Class<? extends IInterceptor>> interceptorDatas, String key) {
        if (!interceptorDatas.containsKey(key)) {
            return false;
        } else {
            Class c = interceptorDatas.get(key);
            try {
                IInterceptor o = (IInterceptor) c.newInstance();
                return o.interceptor();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
