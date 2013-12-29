package de.fanero.gradle.plugin.hadoop

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Test

class HadoopPluginTest {

    public static final String HADOOP_PLUGIN = 'hadoop'

    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: HADOOP_PLUGIN

        Assert.assertNotNull(project.tasks[HadoopPlugin.TASK_START_SCRIPT_NAME])
    }

    @Test
    public void startScriptTaskHasDescription() {
        checkTaskHasDescription(HadoopPlugin.TASK_START_SCRIPT_NAME)
    }

    @Test
    public void tarTaskHasDescription() {
        checkTaskHasDescription(HadoopPlugin.TASK_DIST_TAR_NAME)
    }

    @Test
    public void zipTaskHasDescription() {
        checkTaskHasDescription(HadoopPlugin.TASK_DIST_ZIP_NAME)
    }

    @Test
    public void installTaskHasDescription() {
        checkTaskHasDescription(HadoopPlugin.TASK_INSTALL_NAME)
    }

    private void checkTaskHasDescription(String taskname) {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: HADOOP_PLUGIN

        Assert.assertNotNull(project.tasks[taskname].description)
    }
}
