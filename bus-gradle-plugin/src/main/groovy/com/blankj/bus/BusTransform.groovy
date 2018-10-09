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
        return true
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

        if (!isIncremental) {
            outputProvider.deleteAll()
        }

        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->// 遍历文件夹
                File dest = outputProvider.getContentLocation(
                        directoryInput.name,
                        directoryInput.contentTypes,
                        directoryInput.scopes,
                        Format.DIRECTORY
                )

                FileUtils.copyDirectory(directoryInput.file, dest)
                LogUtils.l(directoryInput.name)
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

                if (!jarName.startsWith('com.android.support:')
                        && !jarName.startsWith('android.arch.')) {
                    if (jarName.startsWith("com.blankj:bus:")) {
                        BusScan.UTIL_CODE_JAR = dest
                    } else {
                        LogUtils.l(jarName)
                        BusScan.SCANS.add(dest)
                    }
                }
            }
        }

        if (BusScan.UTIL_CODE_JAR != null) {
            HashMap<String, String> bus = BusScan.start()
            File jsonFile = new File(Utils.project.rootDir.getAbsolutePath(), "__bus__.json")
            FileUtils.write(jsonFile, JsonUtils.getFormatJson(bus))
            BusInject.start(bus)
        } else {
            LogUtils.l('u should <implementation "com.blankj:utilcode:1.30.+>" ' +
                    'or <implementation "com.blankj:bus:1.0+>')
        }

        LogUtils.l(getName() + " finished: " + (System.currentTimeMillis() - stTime) + "ms")
    }
}