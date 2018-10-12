package com.blankj.bus

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.blankj.util.JsonUtils
import com.blankj.util.LogUtils
import com.blankj.util.Utils
import org.apache.commons.io.FileUtils

class BusTransform extends Transform {

    @Override
    String getName() {
        return "busTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        LogUtils.l(getName() + " started")

        long stTime = System.currentTimeMillis();

        def inputs = transformInvocation.getInputs()
        def referencedInputs = transformInvocation.getReferencedInputs()
        def outputProvider = transformInvocation.getOutputProvider()
        def isIncremental = transformInvocation.isIncremental()

        outputProvider.deleteAll()

        // 加入本地 android 包
        Config.POOL.appendClassPath(Utils.getProject().android.bootClasspath[0].toString())

        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->// 遍历文件夹
                File dest = outputProvider.getContentLocation(
                        directoryInput.name,
                        directoryInput.contentTypes,
                        directoryInput.scopes,
                        Format.DIRECTORY
                )


                FileUtils.copyDirectory(directoryInput.file, dest)
                Config.POOL.appendClassPath(dest.getAbsolutePath())
                LogUtils.l(directoryInput.name + " -> " + dest)

                BusScan.SCANS.add(dest)
            }

            input.jarInputs.each { JarInput jarInput ->// 遍历 jar 文件
                def jarName = jarInput.name
                def dest = outputProvider.getContentLocation(
                        jarName,
                        jarInput.contentTypes,
                        jarInput.scopes,
                        Format.JAR
                )

                FileUtils.copyFile(jarInput.file, dest)
                Config.POOL.appendClassPath(dest.getAbsolutePath())
                LogUtils.l(jarName + " -> " + dest)

                if (!jarName.startsWith('com.android.support:')
                        && !jarName.startsWith('android.arch.')) {
                    if (jarName.startsWith("com.blankj:bus:")) {
                        BusScan.BUS_JAR = dest
                    } else {
                        if (!jarName.startsWith("com.blankj:utilcode:")) {
                            BusScan.SCANS.add(dest)
                        }
                    }
                }
            }
        }

        if (BusScan.BUS_JAR != null) {
            HashMap<String, String> bus = BusScan.start()
            File jsonFile = new File(Utils.project.rootDir.getAbsolutePath(), "__bus__.json")
            String busJson = JsonUtils.getFormatJson(bus)
            FileUtils.write(jsonFile, busJson)
            LogUtils.l(busJson)
            BusInject.start(bus)
        } else {
            LogUtils.l('u should <implementation "com.blankj:utilcode:1.30.+"> ' +
                    'or <implementation "com.blankj:bus:1.0+">')
        }

        LogUtils.l(getName() + " finished: " + (System.currentTimeMillis() - stTime) + "ms")
    }
}