package com.lyl.pluginmanage;

import android.app.Application;
import android.os.Handler;

public interface IAndroidHook extends Handler.Callback {
    void hookAms(Application application) throws Exception;

    void hookActivityThread(Application application) throws Exception;
}
