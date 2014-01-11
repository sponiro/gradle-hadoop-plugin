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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.Sync
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.api.tasks.bundling.Tar
import org.gradle.api.tasks.bundling.Zip

class HadoopPlugin implements Plugin<Project> {

    static final String HADOOP_PLUGIN_NAME = "Hadoop"
    static final String HADOOP_GROUP = HADOOP_PLUGIN_NAME

    static final String TASK_START_SCRIPT_NAME = "hadoopStartScripts"
    static final String TASK_INSTALL_NAME = 'hadoopInstall'
    static final String TASK_DIST_ZIP_NAME = "hadoopDistZip"
    static final String TASK_DIST_TAR_NAME = "hadoopDistTar"

    static final String EXTENSION = 'hadoop'

    private Project project
    private HadoopPluginExtension pluginExtension

    @Override
    void apply(Project project) {
        this.project = project
        project.plugins.apply(JavaPlugin)

        pluginExtension = addPluginExtension()
        addHadoopScriptTask()

        configureApplicationDistribution()

        addInstallTask()
        addDistZipTask()
        addDistTarTask()
    }

    private HadoopPluginExtension addPluginExtension() {
        project.extensions.create(EXTENSION, HadoopPluginExtension, project)
    }

    private void addHadoopScriptTask() {
        HadoopScriptTask task = project.tasks.create(TASK_START_SCRIPT_NAME, HadoopScriptTask)
        task.description = "Creates Un*x specific scripts to run the project as a Hadoop application."
        task.conventionMapping.outputDir = { new File(getHadoopBuildDir(), 'scripts') }
        task.conventionMapping.mainClassName = { pluginExtension.mainClassName }
        task.conventionMapping.applicationName = { getScriptname() }
        task.conventionMapping.libJars = { project.tasks.jar.outputs.files + project.configurations.runtime }
    }

    private String getScriptname() {
        project.name
    }

    private void addInstallTask() {
        Sync task = project.tasks.create(TASK_INSTALL_NAME, Sync)
        task.description = "Installs the project as a Hadoop application along with libs and Un*x specific scripts."
        task.group = HADOOP_GROUP
        task.with pluginExtension.applicationDistribution
        task.into { new File(getHadoopBuildDir(), "install/${pluginExtension.applicationName}") }
        task.doLast {
            project.ant.chmod(file: "${destinationDir.absolutePath}/bin/${getScriptname()}", perm: 'ugo+x')
        }
    }

    private File getHadoopBuildDir() {
        new File(project.buildDir, pluginExtension.buildSubDir)
    }

    private void configureApplicationDistribution() {
        def jar = project.tasks[JavaPlugin.JAR_TASK_NAME]
        def startScript = project.tasks[TASK_START_SCRIPT_NAME]

        pluginExtension.applicationDistribution.with {
            into("lib") {
                from(jar)
                from(project.configurations.runtime)
            }
            into("bin") {
                from(startScript)
                fileMode = 0755
            }
        }
    }

    private void addDistZipTask() {
        addArchiveTask(TASK_DIST_ZIP_NAME, Zip)
    }

    private void addDistTarTask() {
        addArchiveTask(TASK_DIST_TAR_NAME, Tar)
    }

    private <T extends AbstractArchiveTask> void addArchiveTask(String name, Class<T> type) {
        def archiveTask = project.tasks.create(name, type)
        archiveTask.description = "Bundles the project as a Hadoop application with libs and Un*x specific scripts."
        archiveTask.group = HADOOP_GROUP
        archiveTask.conventionMapping.baseName = { getHadoopArchiveName() }
        def baseDir = { archiveTask.archiveName - ".${archiveTask.extension}" }
        archiveTask.into(baseDir) {
            with(pluginExtension.applicationDistribution)
        }
    }

    private String getHadoopArchiveName() {
        pluginExtension.applicationName + ".hadoop"
    }
}
