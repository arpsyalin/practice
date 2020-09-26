package com.lyl.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lyl.butterknife.ButterKnife;
import com.lyl.butterknife.annotations.BindView;
import com.lyl.butterknife.annotations.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(value = R.id.btn_id)
    Button btn_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btn_id.setText("11111");
    }

    @OnClick(value = {R.id.btn_id})
    public void click(View view) {
        // TODO: 2020/9/11 这里换掉ARounder
        //startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
    }
}
