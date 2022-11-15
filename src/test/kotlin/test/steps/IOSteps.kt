package test.steps

import io.cucumber.java.en.And
import org.assertj.core.api.Assertions.assertThat
import test.common.SubmitFeatureContext
import test.common.cleanStringEntry
import java.io.File
import java.nio.file.Files
import kotlin.io.path.createFile
import kotlin.io.path.writeText

class IOSteps {
    private val variables = SubmitFeatureContext.variables
    private val tempFile = Files.createTempDirectory("tempFolder").toFile()

    @And("the file {string} contains:")
    fun assertTheFileContains(fileName: String, content: String) {
        val file = File(cleanStringEntry(fileName))
        require(file.exists())

        assertThat(file.readText()).isEqualTo(content)
    }

    @And("the file {string} named {string} with content")
    fun createFileWithContent(variableName: String, fileName: String, content: String) {
        val file = tempFile.toPath().resolve(fileName).createFile().apply { writeText(content) }

        variables[variableName] = file.toFile()
    }
}
