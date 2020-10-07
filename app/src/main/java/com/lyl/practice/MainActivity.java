package com.lyl.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lyl.arouter.annotations.IsActivity;
import com.lyl.butterknife.ButterKnife;
import com.lyl.butterknife.annotations.BindView;
import com.lyl.butterknife.annotations.OnClick;
import com.lyl.practice.interceptor.LoginInterceptor;

@IsActivity(value = "com.lyl.practice/MainActivity", interceptor = LoginInterceptor.class)
public class MainActivity extends AppCompatActivity {
    @BindView(value = R.id.btn_id)
    Button mBtnId;

    @OnClick(value = {R.id.btn_id})
    public void click(View view) {
        Toast.makeText(this, "点击了", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBtnId.setText("11111");
    }
}
