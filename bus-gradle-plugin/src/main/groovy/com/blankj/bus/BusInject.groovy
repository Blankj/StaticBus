package com.blankj.bus

import com.blankj.util.LogUtils
import com.blankj.util.Utils
import com.blankj.util.ZipUtils
import javassist.CtClass
import javassist.CtMethod
import jdk.internal.org.objectweb.asm.tree.analysis.Value
import org.apache.commons.io.FileUtils

class BusInject {

    static void start(HashMap<String, String> bus) {
        Config.POOL.appendClassPath(Utils.getProject().android.bootClasspath[0].toString())

        File jar = BusScan.UTIL_CODE_JAR
        String jarPath = jar.getAbsolutePath()
        String decompressedJarPath = jarPath.substring(0, jarPath.length() - 4);
        File decompressedJar = new File(decompressedJarPath)
        ZipUtils.unzipFile(jar, decompressedJar)
        Config.POOL.appendClassPath(decompressedJarPath)

        CtClass busUtils = Config.POOL.get(Config.CLASS_BUS_UTILS)
        CtMethod callMethod = busUtils.getDeclaredMethod("post");
        callMethod.insertAfter(getInsertContent(bus));
        busUtils.writeFile(decompressedJarPath)
        FileUtils.forceDelete(jar)
        ZipUtils.zipFile(decompressedJar, jar)
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
                sb.append('return ').append(methodName).append(';\n');
            }
            sb.append("}");
        }
        return sb.toString()
    }
}