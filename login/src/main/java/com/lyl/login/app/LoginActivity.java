package com.lyl.login.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lyl.arouter.annotations.IsActivity;
import com.lyl.butterknife.ButterKnife;
import com.lyl.login.R;

@IsActivity(value = "com.lyl.login/LoginActivity")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_login);
    }
}
