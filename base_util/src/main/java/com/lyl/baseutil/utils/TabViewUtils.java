package com.lyl.baseutil.utils;

import android.graphics.Paint;

import com.google.android.material.tabs.TabLayout;

import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * * @Description
 * * @Author 刘亚林
 * * @CreateDate 2021/8/26
 * * @Version 1.0
 * * @Remark TODO
 **/
public class TabViewUtils {
    public static void changeTabText(TabLayout.Tab tab, float strokeWidth) {
        try {
            Class clazz = Class.forName("com.google.android.material.tabs.TabLayout$TabView");
            Field field = clazz.getDeclaredField("textView");
            field.setAccessible(true);
            Object textView = field.get(tab.view);
            if (textView instanceof TextView) {
                setTextStroke((TextView) textView, strokeWidth);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTextStroke(TextView textView, float strokeWidth) {
        if (textView != null) {
            textView.getPaint().setStrokeWidth(strokeWidth);
            textView.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
        }
    }
}
