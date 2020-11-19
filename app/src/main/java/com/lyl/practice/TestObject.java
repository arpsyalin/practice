package com.lyl.practice;

import android.annotation.SuppressLint;

public final class TestObject {
    @SuppressLint("ResourceType")
    public  TestObject(final MainActivity target) {
        target.mBtnId =(android.widget.Button) target.findViewById(2131165265);
        target.findViewById(2131165265).setOnClickListener(new android.view.View.OnClickListener() { @Override public void onClick(android.view.View v) {target.click(v);}});
        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
