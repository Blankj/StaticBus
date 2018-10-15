package com.blankj.bus

import com.blankj.util.ZipUtils
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.io.FileUtils

class BusInject {

    static void start(HashMap<String, String> bus, File busJar) {
        String jarPath = busJar.getAbsolutePath()
        String decompressedJarPath = jarPath.substring(0, jarPath.length() - 4);
        File decompressedJar = new File(decompressedJarPath)
        ZipUtils.unzipFile(busJar, decompressedJar)

        CtClass busUtils = Config.mPool.get(Config.CLASS_BUS_UTILS)
        CtMethod callMethod = busUtils.getDeclaredMethod("post");
        callMethod.insertAfter(getInsertContent(bus));
        busUtils.writeFile(decompressedJarPath)
        busUtils.defrost();
        FileUtils.forceDelete(busJar)
        ZipUtils.zipFile(decompressedJar, busJar)
        FileUtils.forceDelete(decompressedJar)
    }

    private static String getInsertContent(HashMap<String, String> bus) {
        StringBuilder sb = new StringBuilder();
        bus.each { String key, String val ->
            String name = key
            String[] method = val.split(' ')
            String returnType = method[0]
            String methodName = method[1]

            sb.append('if ("').append(name).append('".equals($1)) {\n');

            int st = methodName.indexOf('(');
            int end = methodName.length();
            String substring = methodName.substring(st + 1, end - 1);
            String[] split = substring.split(",");
            StringBuilder args = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                args.append(',(').append(split[i]).append(')$2[').append(i).append(']');
            }
            methodName = methodName.substring(0, st + 1) + args.substring(1) + ")";

            if (returnType.equals('void')) {
                sb.append(methodName).append(';\n').append('return null;\n');
            } else {
                sb.append('return ($w)').append(methodName).append(';\n');
            }
            sb.append("}");
        }
        sb.append('android.util.Log.e("BusUtils", "bus of <" + $1 + "> didn\'t exist.");');
        return sb.toString()
    }
}