package com.lyl.butterknife;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Constructor;

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
            Object o = constructor.newInstance(a);
            o = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

