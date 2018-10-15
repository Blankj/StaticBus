package com.blankj.bus

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.blankj.util.JsonUtils
import com.blankj.util.LogUtils
import com.blankj.util.Utils
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

import java.util.function.Consumer

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

        Config.initClassPool()
        BusScan busScan = new BusScan()

        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput dirInput ->// 遍历文件夹
                Config.mPool.appendClassPath(dirInput.file.absolutePath)

                busScan.scanDir(dirInput)
            }

            input.jarInputs.each { JarInput jarInput ->// 遍历 jar 文件
                File jar = jarInput.file
                Config.mPool.appendClassPath(jarInput.file.absolutePath)
                def jarName = jarInput.name

                boolean isExcept = false
                for (String except : Config.EXCEPTS) {
                    if (jarName.startsWith(except)) {
                        isExcept = true
                        break
                    }
                }
                if (isExcept) return

                if (jarName.startsWith("com.blankj:bus:")) {
                    def dest = outputProvider.getContentLocation(
                            jarName,
                            jarInput.contentTypes,
                            jarInput.scopes,
                            Format.JAR
                    )
                    FileUtils.copyFile(jar, dest)
                    busScan.busJar = dest
                    return
                }

                busScan.scanJar(jarInput)
            }
        }

        if (busScan.busJar != null) {
            File jsonFile = new File(Utils.project.rootDir.getAbsolutePath(), "__bus__.json")
            String busJson = JsonUtils.getFormatJson(busScan.busMap)
            FileUtils.write(jsonFile, busJson)
            LogUtils.l(busJson)
            BusInject.start(busScan.busMap, busScan.busJar)
        } else {
            LogUtils.l('u should <implementation "com.blankj:utilcode:1.30.+"> ' +
                    'or <implementation "com.blankj:bus:1.0+">')
        }

        LogUtils.l(getName() + " finished: " + (System.currentTimeMillis() - stTime) + "ms")
    }
}