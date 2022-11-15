package test.steps

import io.cucumber.java.en.And
import org.assertj.core.api.Assertions.assertThat
import test.common.SubmitFeatureContext
import test.common.cleanEntry
import java.io.File
import kotlin.io.path.createFile
import kotlin.io.path.name
import kotlin.io.path.writeText

class IOSteps {
    private val variables = SubmitFeatureContext.variables
    private val tempFile = SubmitFeatureContext.tempFile

    @And("the file {string} contains:")
    fun assertTheFileContains(fileName: String, content: String) {
        val file = File(cleanEntry(fileName, variables))
        require(file.exists())

        assertThat(file.readText()).isEqualTo(content)
    }

    @And("the file {string} with content")
    fun createFileWithContent(fileName: String, content: String) {
        val file = tempFile.toPath().resolve(fileName).createFile().apply { writeText(content) }

        variables["fileName"] = file.name
    }
}
