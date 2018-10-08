package com.blankj.bus

import javassist.ClassPool

class Config {

    public static final BUS_EXTENSION = new BusExtension()

    public static final String EXT_NAME = 'bus'

    public static final String PLUGIN_NAME = 'com.blankj.bus'

    public static final String SCAN_CLASS = ''

    public static final String FILE_SEP = System.getProperty("file.separator");

    public static final String CLASS_I_BUS = 'com.blankj.utilcode.util.BusUtils$IBus'

    public static final String CLASS_BUS_UTILS = 'com.blankj.utilcode.util.BusUtils'

    public static final String TYPE_I_BUS = 'com.blankj.utilcode.util.BusUtils.IBus'

    public static final String ANNOTATION_BUS = 'com.blankj.utilcode.util.BusUtils$Bus'

    public static final ClassPool POOL = ClassPool.getDefault()
}