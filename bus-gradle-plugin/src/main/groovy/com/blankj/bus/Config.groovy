package com.blankj.bus

import javassist.ClassPool

class Config {

    public static final BUS_EXTENSION = new BusExtension()

    public static final String EXT_NAME = 'bus'

    public static final String FILE_SEP = System.getProperty("file.separator");

    public static final String CLASS_BUS_UTILS = 'com.blankj.utilcode.util.BusUtils'

    public static final ClassPool POOL = ClassPool.getDefault()
}