package com.lyl.jpush.debug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.lyl.arouter.ARouter;
import com.lyl.arouter.constant.ARouterConstant;
import com.lyl.butterknife.ButterKnife;
import com.lyl.butterknife.annotations.OnClick;
import com.lyl.jpush.R;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        ButterKnife.bind(this);
    }

//    @OnClick(value = R.id.btn_testJump)
    public void onClick(View view) {
        ARouter.getInstance().jumpActivity(this, ARouterConstant.LOGINACTIVITY);
    }
}
