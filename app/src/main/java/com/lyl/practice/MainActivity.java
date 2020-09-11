package com.lyl.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lyl.login.app.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toLogin(View view) {
        // TODO: 2020/9/11 这里换掉ARounder
//        startActivity(new Intent(this, LoginActivity.class));
    }
}
