package com.blankj.staticbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.LogUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtils.e(BusUtils.post("module0", "app to module0"));
        LogUtils.e(BusUtils.post("module1", "app to module1"));
        LogUtils.e(BusUtils.post("lib", "app to lib"));
    }

    public void jump2Module0(View view) {
        LogUtils.e(BusUtils.post("startModule0", this));
    }

    public void jump2Module1(View view) {
        LogUtils.e(BusUtils.post("startModule1", this));
    }
}
