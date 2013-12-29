package de.fanero.gradle.plugin.hadoop

import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.rules.TemporaryFolder

class HadoopStartScriptGeneratorTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder()

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    @Test
    void checkApplicationNameForNull() {
        def generator = createGenerator()
        generator.applicationName = null

        thrown.expect(NullPointerException.class)
        generator.generateUnixScript(testFolder.newFile())

        println testFolder.newFile().text
    }

    @Test
    void checkAppJarForNull() {
        def generator = createGenerator()
        generator.appJar = null

        thrown.expect(NullPointerException.class)
        generator.generateUnixScript(testFolder.newFile())

        println testFolder.newFile().text
    }

    @Test
    void checkLibJarsForNull() {
        def generator = createGenerator()
        generator.libJars = null

        thrown.expect(NullPointerException.class)
        generator.generateUnixScript(testFolder.newFile())

        println testFolder.newFile().text
    }

    @Test
    void generateScript() {
        def script = testFolder.newFile()
        def generator = createGenerator()
        generator.generateUnixScript(script)
    }

    @Test
    void checkScriptApplicationName() {
        String content = createScriptContent()

        Assert.assertThat(content, Matchers.containsString('hadoopTestApplication'))
    }

    @Test
    void checkScriptLibraries() {
        String content = createScriptContent()

        Assert.assertThat(content, Matchers.containsString('library.jar'))
        Assert.assertThat(content, Matchers.containsString('my2ndlibrary.jar'))
    }

    @Test
    void checkScriptApplicationLibrary() {
        String content = createScriptContent()

        Assert.assertThat(content, Matchers.containsString('myJar.jar'))
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
