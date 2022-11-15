package test.common

import java.nio.file.Files

object SubmitFeatureContext {
    val tempFile = Files.createTempDirectory("tempFolder").toFile()
    val variables = mutableMapOf<String, Any>()
    lateinit var responseBody : String
}
