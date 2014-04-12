package de.fanero.gradle.plugin.hadoop

import org.gradle.api.Project
import org.gradle.api.file.CopySpec

class HadoopPluginExtension {

    String applicationName

    String buildSubDir = 'hadoop'

    CopySpec applicationDistribution

    String mainClassName

    boolean exportHadoopClasspath

    HadoopPluginExtension(Project project) {
        applicationName = project.name
        applicationDistribution = project.copySpec {}
    }
}
