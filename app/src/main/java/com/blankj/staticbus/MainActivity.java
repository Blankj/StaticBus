package com.blankj.staticbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.LogUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtils.e(BusUtils.post("module0", true, (byte)-1, Short.MAX_VALUE, 'C', 127, Long.MAX_VALUE, -1f, 2));
        LogUtils.e(BusUtils.post("module1"));
        LogUtils.e(BusUtils.post("lib", "app to lib"));
        LogUtils.e(BusUtils.post("lib1", "app to lib"));
    }

    public void jump2Module0(View view) {
        boolean result = BusUtils.post("startModule0", this, "blankj", 18);
        LogUtils.e(result);
    }

    public void jump2Module1(View view) {
        LogUtils.e(BusUtils.post("startModule1", this));
    }
}
