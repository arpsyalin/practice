package com.lyl.pluginmanage;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

public class NoRegisterActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        Button button = findViewById(R.id.btn_test);
        button.setText("这里是没有注册的Activity");
    }
}
