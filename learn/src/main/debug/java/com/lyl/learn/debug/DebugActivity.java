package com.lyl.learn.debug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.lyl.arouter.ARouter;
import com.lyl.arouter.constant.ARouterConstant;
import com.lyl.butterknife.ButterKnife;
import com.lyl.butterknife.annotations.OnClick;
import com.lyl.learn.R;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        ButterKnife.bind(this);
    }

    @OnClick(value = R.id.btn_testJump)
    public void onClick(View view) {
//        ARouter.getInstance().jumpActivity(this, ARouterConstant.LEARNACTIVITY);
        ARouter.getInstance().jumpActivity(this, ARouterConstant.OPERATINGINSTRUCTIONSACTIVITY);

    }

    @OnClick(value = R.id.btn_go2)
    public void onGo2(View view) {
        ARouter.getInstance().jumpActivity(this, ARouterConstant.OPERATINGINSTRUCTIONSACTIVITY);
    }

}
