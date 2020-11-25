package com.lyl.butterknife;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ButterKnife {
    public static void bind(Activity a) {
        bindInstance(a);
    }

    public static void bind(View a) {
        bindInstance(a);
    }

    public static void bindInstance(Object a) {
        String className = a.getClass().getName() + "$ViewBinding";
        try {
            Class clazz = Class.forName(className);
            Constructor constructor = clazz.getDeclaredConstructor(a.getClass());
            constructor.newInstance(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

