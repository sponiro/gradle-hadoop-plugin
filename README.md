# Gradle Hadoop plugin

A gradle plugin much like the [core application plugin](http://www.gradle.org/docs/current/userguide/application_plugin.html) of gradle but tailored for use with hadoop. As of now its only purpose is to create startup scripts with dependencies in the right place.

## Usage
To use the plugin, add the bintray repository to your script:

```groovy
buildscript {
    repositories {
        mavenCentral()
        maven {
            url 'http://dl.bintray.com/sponiro/gradle-plugins'
        }
    }
    dependencies {
        classpath group: 'de.fanero.gradle.plugin.hadoop', name: 'gradle-hadoop-plugin', version: '0.2'
    }
}

apply plugin: 'hadoop'
```

## Tasks
* `hadoopDistTar` - Bundles the project as a Hadoop application with libs and Un*x specific scripts.
* `hadoopDistZip` - Bundles the project as a Hadoop application with libs and Un*x specific scripts.
* `hadoopInstall` - Installs the project as a Hadoop application along with libs and Un*x specific scripts.

## Convention properties
The convention block is named `hadoop`. The properties available are:

* `applicationName` The application name. At the moment this property is for cosmetical purposes only. Defaults to project.name.
* `buildSubDir` Names a subdirectory to put the application under. We use this to avoid clashes with the gradle application plugin. Defaults to 'hadoop'. This means you can find all built files below `project.buildDir/buildSubDir`.
* `applicationDistribution` A copy spec which defines the files and directories to be created. Works like the [same named property](http://www.gradle.org/docs/current/userguide/application_plugin.html#application_distribution_resources) in the application plugin.
* `mainClassName` Sets the main class to execute. No default.
* `exportHadoopClasspath` true or false. If true, exports the application jars in the environment variable HADOOP_CLASSPATH.

### Example

```groovy
hadoop {
    buildSubDir = "myHadoopSubDir"
    mainClassName = 'de.fanero.HadoopMain'
}
```
