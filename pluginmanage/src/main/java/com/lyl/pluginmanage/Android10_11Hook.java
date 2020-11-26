package com.lyl.pluginmanage;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Message;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//api 29-30
public class Android10_11Hook implements IAndroidHook {

    @Override
    public void hookAms(Application application) throws Exception {
        Class mIActivityTaskManagerClass = Class.forName("android.app.IActivityTaskManager");
        Class mActivityTaskManagerClass = Class.forName("android.app.ActivityTaskManager");
        @SuppressLint("BlockedPrivateApi") Method mGetServiceMethod = mActivityTaskManagerClass.getDeclaredMethod("getService");
        final Object mIActivityManager = mGetServiceMethod.invoke(null);
        Object replaceIActivityManager = Proxy.newProxyInstance(application.getClassLoader(), new Class[]{mIActivityTaskManagerClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("startActivity".equals(method.getName())) {
//                    int startActivity(in IApplicationThread caller, in String callingPackage, in Intent intent,
//                    in String resolvedType, in IBinder resultTo, in String resultWho, int requestCode,
//                    int flags, in ProfilerInfo profilerInfo, in Bundle options);
                    Intent proxyIntent = new Intent();
                    proxyIntent.setClassName("com.lyl.pluginmanage", "com.lyl.pluginmanage.ProxyActivity");
                    proxyIntent.putExtra("targetIntent", ((Intent) args[2]));
                    args[2] = proxyIntent;
                }
                return method.invoke(mIActivityManager, args);
            }
        });

        Class mSingletonClass = Class.forName("android.util.Singleton");
        Field mSingletonField = mSingletonClass.getDeclaredField("mInstance");
        mSingletonField.setAccessible(true);

        Field mIActivityManagerSingletonField = mActivityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton");
        mIActivityManagerSingletonField.setAccessible(true);
        Object mIActivityManagerSingleton = mIActivityManagerSingletonField.get(null);
        mSingletonField.set(mIActivityManagerSingleton, replaceIActivityManager);
    }

    @Override
    public void hookActivityThread(Application application) throws Exception {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
