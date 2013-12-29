package de.fanero.gradle.plugin.hadoop

import com.google.common.base.Preconditions
import groovy.text.SimpleTemplateEngine

class HadoopStartScriptGenerator {

    String applicationName

    String appJar

    List<String> libJars

    File outputDir

    private SimpleTemplateEngine engine = new SimpleTemplateEngine()

    void generateUnixScript(File script) {
        checkPreconditions()

        writeToFile(script)
    }

    void generateUnixScript(Writer writer) {
        checkPreconditions()

        writeToFile(writer)
    }

    private void checkPreconditions() {
        Preconditions.checkNotNull(appJar)
        Preconditions.checkNotNull(applicationName)
        Preconditions.checkNotNull(libJars)
    }

    private void writeToFile(Writer writer) {
        Map<String, String> binding = createBinding()

        Writable output = createUnixScriptContent(binding)
        output.writeTo(writer)
    }

    private void writeToFile(File script) {
        Map<String, String> binding = createBinding()

        script.parentFile.mkdirs()
        script.withWriter {
            Writable output = createUnixScriptContent(binding)
            output.writeTo(it)
        }
    }

    private LinkedHashMap<String, String> createBinding() {
        def binding = [
                applicationName: applicationName,
                appJar: appJar,
                hadoopLibjars: libJars.join(',')
        ]
        binding
    }

    private Writable createUnixScriptContent(HashMap<String, String> binding) {
        def resource = HadoopStartScriptGenerator.getResource("unixHadoopStartScript.txt")
        def templateText = resource.text
        def output = engine.createTemplate(templateText).make(binding)
        output
    }
}
