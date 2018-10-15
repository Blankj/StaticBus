package com.blankj.module0;

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
public class Module0Bus {

    @BusUtils.Subscribe(name = "module0")
    public static String module0Bus(boolean bo,
                                    byte b,
                                    short s,
                                    char c,
                                    int i,
                                    long l,
                                    float f,
                                    double d) {
        LogUtils.e(bo, b, s, c, i, l, f, d);
        return "module0Bus";
    }
}
