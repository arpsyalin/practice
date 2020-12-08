package com.lyl.pluginmanage.hookv;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.lyl.pluginmanage.Constants;
import com.lyl.pluginmanage.IAndroidHook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

//api 21-25
public class Android5_7Hook implements IAndroidHook {
    private final static String TAG = Android5_7Hook.class.getName();

    public Android5_7Hook() {

    }
    @Override
    public void hookAms(Application application) throws Exception {
        Class mActivityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
        Method mGetDefault = mActivityManagerNativeClass.getMethod("getDefault");
        mGetDefault.setAccessible(true);
        final Object mIActivityManager = mGetDefault.invoke(null);
        Class mIActivityManagerClass = Class.forName("android.app.IActivityManager");

        Object replaceIActivityManager = Proxy.newProxyInstance(application.getClassLoader(), new Class[]{mIActivityManagerClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("startActivity".equals(method.getName())) {
//                    public int startActivity(IApplicationThread caller, String callingPackage, Intent intent,
//                            String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags,
//                    ProfilerInfo profilerInfo, Bundle options) throws RemoteException;
                    Log.e(TAG, "InvocationHandler:" + method.getName() + "; args[2]:" + (args[2] instanceof Intent));
                    Intent proxyIntent = new Intent();
                    proxyIntent.setComponent(new ComponentName("com.lyl.pluginmanage", "com.lyl.pluginmanage.ProxyActivity"));
                    proxyIntent.putExtra(Constants.TMP_TARGET_INTENT, ((Intent) args[2]));
                    args[2] = proxyIntent;
                }
                return method.invoke(mIActivityManager, args);
            }
        });
        Field mGDefaultField = mActivityManagerNativeClass.getDeclaredField("gDefault");
        mGDefaultField.setAccessible(true);
        final Object mIActivityManagerInstance = mGDefaultField.get(null);
        //Singleton Class
        Class mSingletonClass = Class.forName("android.util.Singleton");
        //获取mInstance Field
        Field mSingletonField = mSingletonClass.getDeclaredField("mInstance");
        mSingletonField.setAccessible(true);
        mSingletonField.set(mIActivityManagerInstance, replaceIActivityManager);
    }

    @Override
    public void hookActivityThread(Application application) throws Exception {
//        private static volatile ActivityThread sCurrentActivityThread;
//        public static ActivityThread currentActivityThread() {
//            return sCurrentActivityThread;
//        }
        Class mActivityThreadClass = Class.forName("android.app.ActivityThread");
        Method mCurrentActivityThreadMethod = mActivityThreadClass.getMethod("currentActivityThread");
        Object mActivityThreadObject = mCurrentActivityThreadMethod.invoke(null);
//        Method mGetHandlerMethod = mActivityThreadClass.getMethod("getHandler");
//        mGetHandlerMethod.setAccessible(true);
//        Object mH = mGetHandlerMethod.invoke(mActivityThreadObject);
        Field mGetHandlerField = mActivityThreadClass.getDeclaredField("mH");
        mGetHandlerField.setAccessible(true);
        Object mH = mGetHandlerField.get(mActivityThreadObject);
        Class mHandlerClass = Class.forName("android.os.Handler");
        Field mCallbackField = mHandlerClass.getDeclaredField("mCallback");
        mCallbackField.setAccessible(true);
        mCallbackField.set(mH, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (Constants.LAUNCH_ACTIVITY == msg.what) {
//            final ActivityClientRecord r = (ActivityClientRecord) msg.obj;
//
//            ActivityClientRecord {
//            IBinder token;
//            int ident;
//            Intent intent;
            try {
//                Log.e(TAG, "________________________________________________");
                Class mActivityClientRecordClass = Class.forName("android.app.ActivityThread$ActivityClientRecord");
                Field mIntentField = mActivityClientRecordClass.getDeclaredField("intent");
                mIntentField.setAccessible(true);
                Intent proxyIntent = (Intent) mIntentField.get(msg.obj);
                Intent intent = proxyIntent.getParcelableExtra(Constants.TMP_TARGET_INTENT);
//                Log.e(TAG, proxyIntent.getComponent().toString());
//                Log.e(TAG, intent.getComponent().toString());
                if (intent != null) {
                    mIntentField.set(msg.obj, intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
//                Log.e(TAG, "________________________________________________" + e.getMessage());
            }
        }
        return false;
    }
}
