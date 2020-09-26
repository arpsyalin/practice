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
        String viewBindingClassName = a.getClass().getName() + "$ViewBinding";
        try {
            Class viewBindingClass = Class.forName(viewBindingClassName);
            Constructor viewBindingConstructor = viewBindingClass.getDeclaredConstructor(a.getClass());
            Object o = viewBindingConstructor.newInstance(a);
//            o = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
