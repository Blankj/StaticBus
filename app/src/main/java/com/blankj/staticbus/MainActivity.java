package com.blankj.staticbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
}
