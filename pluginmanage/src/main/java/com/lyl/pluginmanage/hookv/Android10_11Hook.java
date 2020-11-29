package com.lyl.pluginmanage.hookv;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.lyl.pluginmanage.Constants;
import com.lyl.pluginmanage.IAndroidHook;
import com.lyl.pluginmanage.reflectionlimit.ReflectionLimit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
//import android.app.IActivityTaskManager;

//api 29-30
public class Android10_11Hook implements IAndroidHook {
//    private static final Singleton<IActivityTaskManager> IActivityTaskManagerSingleton =
//            new Singleton<IActivityTaskManager>() {
//                @Override
//                protected IActivityTaskManager create() {
//                    final IBinder b = ServiceManager.getService(Context.ACTIVITY_TASK_SERVICE);
//                    return IActivityTaskManager.Stub.asInterface(b);
//                }
//            };

    @Override
    public void hookAms(Application application) throws Exception {
        ReflectionLimit.clearLimit();
//        https://www.androidos.net.cn/android/10.0.0_r6/xref/frameworks/base/core/java/android/app/IActivityTaskManager.aidl
//        int startActivity(in IApplicationThread caller, in String callingPackage, in Intent intent,
//        in String resolvedType, in IBinder resultTo, in String resultWho, int requestCode,
//        int flags, in ProfilerInfo profilerInfo, in Bundle options);
////      尝试从ServiceManager里面反射出一个
//        Class mServiceManagerClass = Class.forName("android.os.ServiceManager");
//        Method mGetServiceManagerMethod = mServiceManagerClass.getDeclaredMethod("getService", new Class[]{String.class});
//        mGetServiceManagerMethod.setAccessible(true);
//        final Object mIActivityManager1 = mGetServiceManagerMethod.invoke(null, "activity_task");
//        Class mStubClass = Class.forName("android.app.IActivityTaskManager$Stub");
//        Class mIBinderClass = Class.forName("android.os.BinderProxy");
//        Method mAsInterfaceMethod = mIBinderClass.getDeclaredMethod("queryLocalInterface", new Class[]{String.class});
//        mAsInterfaceMethod.setAccessible(true);
//        final Object mIActivityManager = mAsInterfaceMethod.invoke(mIActivityManager1, "activity_task");
//        //尝试从ServiceManager里面反射出一个失败
        Class mIActivityTaskManagerClass = Class.forName("android.app.IActivityTaskManager");
        Class mActivityTaskManagerClass = Class.forName("android.app.ActivityTaskManager");
        Method mGetServiceMethod = mActivityTaskManagerClass.getDeclaredMethod("getService", new Class[]{});
//        mGetServiceMethod.setAccessible(true);
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
//        Field mIActivityManagerSingletonField = mActivityTaskManagerClass.getDeclaredField("IActivityManagerSingleton");
        Field mIActivityManagerSingletonField = mActivityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton");
        mIActivityManagerSingletonField.setAccessible(true);
        Object mIActivityManagerSingleton = mIActivityManagerSingletonField.get(null);
        mSingletonField.set(mIActivityManagerSingleton, replaceIActivityManager);
    }

    @Override
    public void hookActivityThread(Application application) throws Exception {
        //https://www.androidos.net.cn/android/10.0.0_r6/xref/frameworks/base/core/java/android/app/ActivityThread.java
        Class mActivityThreadClass = Class.forName("android.app.ActivityThread");
        Object mActivityThread = mActivityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field mHField = mActivityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        Object mH = mHField.get(mActivityThread);
//        final Handler.Callback mCallback;
        Field mCallbackField = Handler.class.getDeclaredField("mCallback");
        mCallbackField.setAccessible(true);
        //自己处理handleMessage
        mCallbackField.set(mH, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (Constants.EXECUTE_TRANSACTION == msg.what) {
//        case EXECUTE_TRANSACTION:
//        final ClientTransaction transaction = (ClientTransaction) msg.obj;
//        mTransactionExecutor.execute(transaction);
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
