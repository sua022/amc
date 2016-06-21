package cn.hugoz.amc.plugin;

import org.gradle.api.Project

class AMCExtension {

    private def moduleSdkPath;
    private def rootPrj;
    private export = false;
    private def annotation;

    AMCExtension(Project project) {
        if (project.rootProject) {
            rootPrj = project.rootProject;
        } else {
            rootPrj = project;
        }
        moduleSdkPath = rootPrj.buildDir.absolutePath + "/amcsdk/"
    }


    def amcSdk(moduleName) {
        return rootPrj.fileTree(dir:moduleSdkPath + moduleName+"/gen/",include: ['*.jar'])
    }

    def amcSdkPath() {
        return moduleSdkPath;
    }

    def annotation(name) {
        annotation = name;
    }

    def export(boolean e) {
        export = e
    }

    def isExport() {
        return export;
    }

    def getAnnotation() {
        return annotation;
    }

}