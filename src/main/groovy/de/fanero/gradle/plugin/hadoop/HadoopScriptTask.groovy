/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    String mainClassName

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
        generator.mainClassName = getMainClassName()
        generator.appJar = getLibPrefix() + project.tasks.jar.archiveName
        generator.libJars = getLibJars().collect { getLibPrefix() + it.name }
        generator.outputDir = getOutputDir()
        generator.generateUnixScript(getUnixScriptName())
    }
}
