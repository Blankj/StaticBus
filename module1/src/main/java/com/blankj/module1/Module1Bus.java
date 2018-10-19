package com.blankj.module1;

import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.LogUtils;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/10/08
 *     desc  :
 * </pre>
 */
public class Module1Bus {

    @BusUtils.Subscribe(name = "module1")
    public static void module1Bus() {
        LogUtils.e("module1");
    }
}
