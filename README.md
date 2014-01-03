# Gradle Hadoop Plugin

A gradle plugin much like the core application plugin of gradle but tailored for use with hadoop. As of now it's only purpose is to create startup scripts with dependencies in the right place.

## Usage
For the moment you have to checkout the plugin and do a `gradle install` to install it into your local maven repository. Make it available to your script:

```groovy
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath group: 'de.fanero.gradle.plugin.hadoop', name: 'gradle-hadoop-plugin', version: '0.1'
    }
}
```

```groovy
apply plugin: 'hadoop'
```

## Tasks
* `hadoopDistTar` - Bundles the project as a Hadoop application with libs and Un*x specific scripts.
* `hadoopDistZip` - Bundles the project as a Hadoop application with libs and Un*x specific scripts.
* `hadoopInstall` - Installs the project as a Hadoop application along with libs and Un*x specific scripts.

