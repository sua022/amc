package cn.hugoz.amc.plugin

import cn.hugoz.amc.SdkGenerator;
import org.gradle.api.Plugin
import org.gradle.api.Project

class AMCPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.extensions.create("amc", AMCExtension, project)

        project.afterEvaluate {
            //println("after ${project.name} evaluate...")
            def annotationName = project.amc.getAnnotation()
            if (project.amc.isExport() && annotationName != null && !annotationName.equals("")) {
                def moudleSdkPath = "${project.amc.amcSdkPath()}${project.name}"
                def destClassDir = "${moudleSdkPath}/classes/"
                project.android.sourceSets.main.java.srcDirs.each { dir ->
                    //println("start parse ${dir} files ")
                    new SdkGenerator().run(dir,annotationName,destClassDir)
                }
                def jarOutput = moudleSdkPath + "/gen/"
                def jarOutputFile = new File(jarOutput)
                if (!jarOutputFile.exists()) {
                    jarOutputFile.mkdirs()
                }

                def jarPath = jarOutput + project.name +".jar"
                def jarCmd = "jar cvf ${jarPath} -C ${destClassDir} ."
                def proc = jarCmd.execute()
                proc.waitFor()

//                def dst = new File(dir + '/qmi_plugin_sdk.jar')
//                dst << src.bytes
            }
        }
    }

}