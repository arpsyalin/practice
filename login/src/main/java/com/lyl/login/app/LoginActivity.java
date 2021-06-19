package com.lyl.login.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.lyl.arouter.annotations.IsActivity;
import com.lyl.butterknife.ButterKnife;
import com.lyl.butterknife.annotations.OnClick;
import com.lyl.login.R;

@IsActivity(value = "com.lyl.login/LoginActivity")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

//    @OnClick(value = R.id.btn_loginSuccess)
    public void onLoginSuccessCallBack(View v) {

    }

//    @OnClick(value = R.id.btn_loginFail)
    public void onLoginFailCallBack(View v) {

    }
}
