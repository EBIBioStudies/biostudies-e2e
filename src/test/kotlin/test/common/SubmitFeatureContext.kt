package test.common

import org.springframework.http.HttpHeaders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.nio.file.Files

object SubmitFeatureContext {

    val restTemplate = RestTemplate()
    val variables: MutableMap<String, String> = mutableMapOf()
    val tempFile = Files.createTempDirectory("tempFolder").toFile()
    var formDataBodyRequest = LinkedMultiValueMap<String, Any>()
    val headers = HttpHeaders()
}
