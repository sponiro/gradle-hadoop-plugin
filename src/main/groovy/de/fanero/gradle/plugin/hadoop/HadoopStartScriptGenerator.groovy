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

import com.google.common.base.Preconditions
import groovy.text.SimpleTemplateEngine

class HadoopStartScriptGenerator {

    String applicationName

    String mainClassName

    String appJar

    List<String> libJars

    File outputDir

    boolean exportHadoopClasspath

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
                hadoopLibjars: libJars.join(','),
                exportHadoopClasspath: exportHadoopClasspath ? libJars.join(":") : "",
                mainClassName: mainClassName
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
