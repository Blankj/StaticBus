package com.blankj.bus

import com.android.build.api.transform.QualifiedContent
import com.blankj.util.LogUtils
import com.blankj.util.ZipUtils
import com.blankj.utilcode.util.BusUtils
import groovy.io.FileType
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.io.FileUtils

class BusScan {


    HashMap<String, String> busMap
    List<File> scans
    File busJar

    BusScan() {
        busMap = [:]
        scans = []
    }

    void scanJar(QualifiedContent content) {
        File jar = content.file
        def tmp = new File(jar.getParent(), "temp_" + jar.getName())
        ZipUtils.unzipFile(jar, tmp)
        scanDir(tmp)
        FileUtils.forceDelete(tmp)
    }

    void scanDir(QualifiedContent content) {
        File root = content.file
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

                CtClass ctClass = Config.mPool.get(className)

                CtMethod[] methods = ctClass.getDeclaredMethods();
                for (CtMethod method : methods) {
                    if (method.hasAnnotation(BusUtils.Subscribe)) {
                        busMap.put(method.getAnnotation(BusUtils.Subscribe).name(),
                                method.getReturnType().getName() + ' ' + method.getLongName()
                        )
//                        LogUtils.l(content.name + " -> " + dest)
                    }
                }
            }
        }
    }
}
