package com.blankj.module0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.BusUtils;

public class Module0Activity extends Activity {

    @BusUtils.Subscribe(name = "startModule0")
    public static boolean start1(Context context) {
        Intent starter = new Intent(context, Module0Activity.class);
        context.startActivity(starter);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module0);
    }
}
