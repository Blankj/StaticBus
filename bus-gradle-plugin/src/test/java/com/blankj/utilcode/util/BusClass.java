package com.blankj.utilcode.util;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/09/22
 *     desc  :
 * </pre>
 */
@BusUtils.Bus
public class BusClass {

    @BusUtils.Subscribe(name = "busClass")
    public static String getName(String name, Object... objects) {
        System.out.println("haha");
        return "haha";
    }

    public static void noAnnotation() {
        System.out.println("haha");
    }
}
