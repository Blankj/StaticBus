package com.blankj.bus

import com.blankj.util.ZipUtils
import com.blankj.utilcode.util.BusUtils
import groovy.io.FileType
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.io.FileUtils

class BusScan {

    private static HashMap<String, String> BUS_MAP = [:]
    public static List<File> SCANS = []
    public static File UTIL_CODE_JAR = null

    static HashMap<String, String> start() {
        BUS_MAP.clear()
        SCANS.each { File file ->
            Config.POOL.appendClassPath(file.absolutePath)
            if (file.name.endsWith(".jar")) {// process jar
                scanJar(file)
            } else {
                scanDir(file)
            }
        }
        return BUS_MAP
    }

    private static void scanJar(File jar) {
        def tmp = new File(jar.getParent(), "temp_" + jar.getName())
        ZipUtils.unzipFile(jar, tmp)
        scanDir(tmp)
        FileUtils.forceDelete(tmp)
    }

    private static void scanDir(File root) {
        String rootPath = root.getAbsolutePath()
        if (!rootPath.endsWith(Config.FILE_SEP)) {
            rootPath += Config.FILE_SEP
        }

        if (root.isDirectory()) {
            root.eachFileRecurse(FileType.FILES) { File file ->
                def fileName = file.name
                if (!fileName.endsWith('.class')
                        || fileName.startsWith('R$')
                        || fileName == 'R.class'
                        || fileName == 'BuildConfig.class') {
                    return
                }

                def filePath = file.absolutePath
                def packagePath = filePath.replace(rootPath, '')
                def className = packagePath.replace(Config.FILE_SEP, ".")
                // delete .class
                className = className.substring(0, className.length() - 6)

                CtClass ctClass = Config.POOL.get(className)
                if (!ctClass.hasAnnotation(BusUtils.Bus)) return

                CtMethod[] methods = ctClass.getDeclaredMethods();
                for (CtMethod method : methods) {
                    if (method.hasAnnotation(BusUtils.Subscribe)) {
                        BUS_MAP.put(method.getAnnotation(BusUtils.Subscribe).name(),
                                method.getReturnType().getName() + ' ' + method.getLongName()
                        )
                    }
                }
            }
        }
    }
}
