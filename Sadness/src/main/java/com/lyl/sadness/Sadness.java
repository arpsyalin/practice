package com.lyl.sadness;

import android.app.Activity;
import android.app.Application;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Sadness implements Application.ActivityLifecycleCallbacks {
    static Sadness instance;
    Paint mPaint = new Paint();

    public static Sadness getInstance() {
        if (instance == null) instance = new Sadness();
        return instance;
    }

    boolean isGary = false;

    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    public void deInit(Application application) {
        application.unregisterActivityLifecycleCallbacks(this);
    }

    public void setGary(boolean gary) {
        isGary = gary;
    }

    private boolean getColorFilterCache() {
        //网络弄下来缓存
        return isGary;
    }

    private void changeColorFilter(Activity activity) {
        if (getColorFilterCache()) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setLayerType(View.LAYER_TYPE_HARDWARE, mPaint);
        }
    }

    private Sadness() {
        ColorMatrix mColorMatrix = new ColorMatrix();
        mColorMatrix.setSaturation(0);
        mPaint.setColorFilter(new ColorMatrixColorFilter(mColorMatrix));
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        changeColorFilter(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
