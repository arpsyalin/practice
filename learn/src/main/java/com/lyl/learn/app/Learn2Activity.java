package com.lyl.learn.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.lyl.base_app.BaseActivity;
import com.lyl.learn.R;

public class Learn2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_learn2);
        initView();
    }

    private void initView() {

    }
}