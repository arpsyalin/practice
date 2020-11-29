package com.lyl.pluginmanage.hookv;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lyl.pluginmanage.Constants;
import com.lyl.pluginmanage.IAndroidHook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

//api 26-28
public class Android8_9Hook implements IAndroidHook {
    private final static String TAG = Android8_9Hook.class.getName();

    @Override
    public void hookAms(final Application application) throws Exception {
        //获取ActivityManager Class
        Class mActivityManagerClass = Class.forName("android.app.ActivityManager");
        //IActivityManager Class
        Class mIActivityManagerClass = Class.forName("android.app.IActivityManager");
        Method mGetServiceMethod = mActivityManagerClass.getDeclaredMethod("getService");
        final Object mIActivityManager = mGetServiceMethod.invoke(null);

        Object replaceIActivityManager = Proxy.newProxyInstance(application.getClassLoader(), new Class[]{mIActivityManagerClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("startActivity".equals(method.getName())) {
                    Log.e(TAG, "InvocationHandler:" + method.getName() + "; args[2]:" + (args[2] instanceof Intent));
                    Intent proxyIntent = new Intent();
                    proxyIntent.setComponent(new ComponentName("com.lyl.pluginmanage", "com.lyl.pluginmanage.ProxyActivity"));
                    proxyIntent.putExtra(Constants.TMP_TARGET_INTENT, ((Intent) args[2]));
                    args[2] = proxyIntent;
                }
                return method.invoke(mIActivityManager, args);
            }
        });
        Field mIActivityManagerSingletonField = mActivityManagerClass.getDeclaredField("IActivityManagerSingleton");
        mIActivityManagerSingletonField.setAccessible(true);
        Object mIActivityManagerSingleton = mIActivityManagerSingletonField.get(null);
        //Singleton Class
        Class mSingletonClass = Class.forName("android.util.Singleton");
        //获取mInstance Field
        Field mSingletonField = mSingletonClass.getDeclaredField("mInstance");
        mSingletonField.setAccessible(true);
        mSingletonField.set(mIActivityManagerSingleton, replaceIActivityManager);
    }

    @Override
    public void hookActivityThread(Application application) throws Exception {
//        https://www.androidos.net.cn/android/8.0.0_r4/xref/frameworks/base/core/java/android/app/ActivityThread.java
        Class mActivityThreadClass = Class.forName("android.app.ActivityThread");
        Object mActivityThread = mActivityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field mHField = mActivityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        Object mH = mHField.get(mActivityThread);
        Field mCallbackField = Handler.class.getDeclaredField("mCallback");
        mCallbackField.setAccessible(true);
        //自己处理handleMessage
        mCallbackField.set(mH, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (Constants.EXECUTE_TRANSACTION == msg.what) {
                /*final ClientTransaction transaction = (ClientTransaction) msg.obj;
                mTransactionExecutor.execute(transaction);*/
            Object mClientTransaction = msg.obj;
            try {
                // Field mActivityCallbacksField = mClientTransaction.getClass().getDeclaredField("mActivityCallbacks");
                Class<?> mClientTransactionClass = Class.forName("android.app.servertransaction.ClientTransaction");
                Field mActivityCallbacksField = mClientTransactionClass.getDeclaredField("mActivityCallbacks");
                mActivityCallbacksField.setAccessible(true);
                // List<ClientTransactionItem> mActivityCallbacks;
                List mActivityCallbacks = (List) mActivityCallbacksField.get(mClientTransaction);
                // TODO 需要判断
                if (mActivityCallbacks.size() == 0) {
                    return false;
                }
                Object mLaunchActivityItem = mActivityCallbacks.get(0);

                Class mLaunchActivityItemClass = Class.forName("android.app.servertransaction.LaunchActivityItem");
                // TODO 需要判断
                if (!mLaunchActivityItemClass.isInstance(mLaunchActivityItem)) {
                    return false;
                }
                Field mIntentField = mLaunchActivityItemClass.getDeclaredField("mIntent");
                mIntentField.setAccessible(true);
                // 拿到真实的Intent
                Intent proxyIntent = (Intent) mIntentField.get(mLaunchActivityItem);
                Intent targetIntent = proxyIntent.getParcelableExtra(Constants.TMP_TARGET_INTENT);
                if (targetIntent != null) {
                    //把存起来的targetIntent换回来完成跳转
                    mIntentField.set(mLaunchActivityItem, targetIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
