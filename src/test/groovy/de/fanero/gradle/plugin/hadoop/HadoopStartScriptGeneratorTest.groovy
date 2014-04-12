/*
 * Copyright 2014 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fanero.gradle.plugin.hadoop

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.rules.TemporaryFolder

import static org.hamcrest.Matchers.containsString
import static org.hamcrest.Matchers.not
import static org.junit.Assert.assertThat

class HadoopStartScriptGeneratorTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    @Test
    void checkApplicationNameForNull() {
        def generator = createGenerator()
        generator.applicationName = null

        thrown.expect(NullPointerException.class)
        generator.generateUnixScript(folder.newFile())
    }

    @Test
    void checkAppJarForNull() {
        def generator = createGenerator()
        generator.appJar = null

        thrown.expect(NullPointerException.class)
        generator.generateUnixScript(folder.newFile())
    }

    @Test
    void checkLibJarsForNull() {
        def generator = createGenerator()
        generator.libJars = null

        thrown.expect(NullPointerException.class)
        generator.generateUnixScript(folder.newFile())
    }

    @Test
    void generateScript() {
        def script = folder.newFile()
        def generator = createGenerator()
        generator.generateUnixScript(script)
    }

    @Test
    void checkScriptApplicationName() {
        String content = createScriptContent()

        assertThat(content, containsString('hadoopTestApplication'))
    }

    @Test
    void checkScriptLibraries() {
        String content = createScriptContent()

        assertThat(content, containsString('library.jar'))
        assertThat(content, containsString('my2ndlibrary.jar'))
    }

    @Test
    void checkScriptApplicationLibrary() {
        String content = createScriptContent()

        assertThat(content, containsString('myJar.jar'))
    }

    @Test
    void checkScriptMainClassName() {
        HadoopStartScriptGenerator generator = createGenerator()
        generator.mainClassName = "HadoopMain"

        StringWriter writer = new StringWriter()
        generator.generateUnixScript(writer)

        assertThat(writer.toString(), containsString('HadoopMain'))
    }

    @Test
    void checkScriptEmptyMainClassName() {
        HadoopStartScriptGenerator generator = createGenerator()
        generator.mainClassName = ""

        StringWriter writer = new StringWriter()
        generator.generateUnixScript(writer)

        assertThat(writer.toString(), not(containsString('HadoopMain')))
    }

    @Test
    void checkScriptNoExport() {
        HadoopStartScriptGenerator generator = createGenerator()

        StringWriter writer = new StringWriter()
        generator.generateUnixScript(writer)

        assertThat(writer.toString(), not(containsString('export')))
    }

    @Test
    void checkScriptExport() {
        HadoopStartScriptGenerator generator = createGenerator()
        generator.exportHadoopClasspath = true

        StringWriter writer = new StringWriter()
        generator.generateUnixScript(writer)

        assertThat(writer.toString(), containsString('export'))
        println writer.toString()
    }

    @Test
    void checkScriptNullMainClassName() {
        HadoopStartScriptGenerator generator = createGenerator()
        generator.mainClassName = null

        StringWriter writer = new StringWriter()
        generator.generateUnixScript(writer)

        assertThat(writer.toString(), not(containsString('HadoopMain')))
    }

    private String createScriptContent() {
        def writer = new StringWriter()

        def generator = createGenerator()
        generator.generateUnixScript(writer)
        writer.toString()
    }

    private HadoopStartScriptGenerator createGenerator() {
        def generator = new HadoopStartScriptGenerator()
        generator.applicationName = "hadoopTestApplication"
        generator.libJars = ["library.jar", "my2ndlibrary.jar"]
        generator.appJar = "myJar.jar"
        generator
    }
}
