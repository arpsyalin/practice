package com.lyl.practice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lyl.arouter.annotations.IsActivity;
import com.lyl.arouter.livedata.ALiveDataFactory;
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
        new TestObject(this);
//        ButterKnife.bind(this);
//        MainActivity$ViewBinding mainActivity$ViewBinding =     new MainActivity$ViewBinding(this);
        mBtnId.setText("11111");
//        Intent intent = getIntent();
//        intent.getA();
        ALiveDataFactory.getInstance().with("aa111", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
//        mainActivity$ViewBinding=null;
    }
}
