package de.fanero.gradle.plugin.hadoop

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class HadoopScriptTask extends DefaultTask {

    @Input
    String applicationName

    @Input
    FileCollection libJars

    @Input
    @Optional
    String libPrefix = '../lib/'

    File outputDir

    @OutputFile
    File getUnixScriptName() {
        new File(getOutputDir(), getApplicationName())
    }

    @TaskAction
    void generate() {
        def generator = new HadoopStartScriptGenerator()
        generator.applicationName = getApplicationName()
        generator.appJar = getLibPrefix() + project.tasks.jar.archiveName
        generator.libJars = getLibJars().collect { getLibPrefix() + it.name }
        generator.outputDir = getOutputDir()
        generator.generateUnixScript(getUnixScriptName())
    }
}
