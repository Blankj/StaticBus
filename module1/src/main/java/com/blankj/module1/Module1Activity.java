package com.blankj.module1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.BusUtils;

public class Module1Activity extends Activity {

    @BusUtils.Subscribe(name = "startModule1")
    public static boolean start(Context context) {
        Intent starter = new Intent(context, Module1Activity.class);
        context.startActivity(starter);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module1);
    }
}
