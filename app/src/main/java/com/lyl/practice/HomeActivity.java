package com.lyl.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lyl.arouter.ARouter;
import com.lyl.arouter.constant.ARouterConstant;
import com.lyl.butterknife.ButterKnife;
import com.lyl.butterknife.annotations.BindView;
import com.lyl.butterknife.annotations.OnClick;
import com.lyl.ifw.DealFactory;
import com.lyl.ifw.IView;
import com.lyl.ifw.RequestMapBuilder;

public class HomeActivity extends AppCompatActivity implements IView<String> {
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
        ARouter.getInstance().jumpActivity(this, ARouterConstant.MAINACTIVITY);
        DealFactory.getInstance().request(this, DealFactory.getInstance().clone(), String.class);
    }


    @Override
    public void notifyResult(final int resultCode, RequestMapBuilder requestMapBuilder, final String callBackData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HomeActivity.this, "1111" + resultCode + ";" + callBackData, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
