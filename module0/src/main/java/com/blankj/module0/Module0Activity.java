package com.blankj.module0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.BusUtils;

public class Module0Activity extends Activity {

    public static boolean start(Context context, String name) {
        Intent starter = new Intent(context, Module0Activity.class);
        starter.putExtra("name", name);
        context.startActivity(starter);
        return true;
    }

    @BusUtils.Subscribe(name = "startModule0")
    public static boolean start(Context context, String name, int age) {
        Intent starter = new Intent(context, Module0Activity.class);
        starter.putExtra("name", name);
        starter.putExtra("age", age);
        context.startActivity(starter);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module0);
    }
}
