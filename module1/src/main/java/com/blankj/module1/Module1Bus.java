package com.blankj.module1;

import com.blankj.common.Common;
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
@BusUtils.Bus
public class Module1Bus {

    @BusUtils.Subscribe(name = "module1")
    public static String module1Bus(String name) {
        LogUtils.e(name);
        return "module1Bus";
    }
}
