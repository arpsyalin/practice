package com.lyl.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lyl.arouter.ARouter;
import com.lyl.butterknife.ButterKnife;
import com.lyl.butterknife.annotations.BindView;
import com.lyl.butterknife.annotations.OnClick;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.btn_to_main)
    Button mJumpMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

        @OnClick(R.id.btn_to_main)
    public void jumpMain(View v) {
        ARouter.getInstance().jumpActivity(this, "com.lyl.practice/MainActivity");
    }
}
