package test.steps

import io.cucumber.java.en.And
import org.assertj.core.api.Assertions.assertThat
import org.springframework.core.io.FileSystemResource
import test.common.ContextVariables
import test.common.ContextVariables.getString
import java.io.File
import java.nio.file.Files
import kotlin.io.path.createFile
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists
import kotlin.io.path.writeText

class IOSteps {
    private val tempFile = Files.createTempDirectory("tempFolder").toFile()

    @And("the file {string} has content:")
    fun assertFileContent(fileName: String, content: String) {
        val file = File(getString(fileName))
        require(file.exists())

        assertThat(file.readText()).isEqualTo(content)
    }

    @And("the file {string} named {string} with content")
    fun createFileWithContent(variableName: String, fileName: String, content: String) {
        val file = tempFile.toPath().resolve(fileName).createFile().apply { writeText(content) }

        ContextVariables[variableName] = FileSystemResource(file.toFile())
    }

    @And("the file {string} named {string} is modified with the new content")
    fun changeFile(variableName: String, fileName: String, newContent: String) {
        val file = tempFile.toPath().resolve(fileName)
        require(file.exists())
        file.deleteExisting()
        val newFile = file.createFile().apply { writeText(newContent) }

        ContextVariables[variableName] = FileSystemResource(newFile.toFile())
    }
}
