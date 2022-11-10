package test.steps

import com.jayway.jsonpath.JsonPath
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.MediaType.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import test.common.*
import java.nio.file.Files
import java.util.*
import kotlin.io.path.createFile
import kotlin.io.path.name
import kotlin.io.path.writeText

class SubmitSteps {
    private val restTemplate = RestTemplate()
    private val variables: MutableMap<String, String> = mutableMapOf()
    private val tempFile = Files.createTempDirectory("tempFolder").toFile()
    private var formDataBodyRequest = LinkedMultiValueMap<String, Any>()
    private val headers = HttpHeaders()

    @Given("the setup information")
    fun theSetupInformation(table: DataTable) {
        variables.putAll(table.asMap())
    }

    @Given("a http request with body:")
    fun defineBodyRequest(body: String) {
        val cleanedBody = cleanEntry(body, variables)
        variables[REQUEST_BODY] = cleanedBody
    }

    @And("url path {string}")
    fun setUrlPath(path: String) {
        val cleanedUrlPath = cleanEntry(path, variables)

        variables[REQUEST_URL] = cleanedUrlPath
    }

    @And("http method {string}")
    fun setHttpMethod(method: String) {
        variables[HTTP_METHOD] = method
    }

    @When("json request is performed")
    fun performJsonRequest() {
        headers.clean().addPlainTextContentType()
        val httpEntity = HttpEntity(variables[REQUEST_BODY], headers)

        val response =
            restTemplate.postForEntity(requireNotNull(variables[REQUEST_URL]), httpEntity, String::class.java)

        variables[HTTP_STATUS_CODE] = response.statusCodeValue.toString()
        variables[RESPONSE_BODY] = requireNotNull(response.body)
    }

    @Then("http status code {string} is returned")
    fun getHttpCode(statusCode: String) {
        assertThat(variables[HTTP_STATUS_CODE]).isEqualTo(statusCode)
    }

    @And("http response the JSONPath value {string} is saved into {string}")
    fun fromHttpResponseIsExtractByJSONPathAndSaveInVariableNamed(jsonPath: String, name: String) {
        val value = JsonPath.read<String>(variables[RESPONSE_BODY], jsonPath)

        variables[TOKEN_SESSION_ID] = value
    }

    @And("the file {string} with content")
    fun createFileWithContent(fileName: String, content: String) {
        val file = tempFile.toPath().resolve(fileName).createFile().apply { writeText(content) }

        variables["fileName"] = file.name
    }

    @And("a http request with form-data body:")
    fun setBodyInFormData(bodyTable: DataTable) {
        val map = bodyTable.asMap()
        val file = tempFile.resolve(requireNotNull(map["files"]).removePrefix("$")).canonicalFile

        formDataBodyRequest.add("files", FileSystemResource(file))
    }

    @And("header(s)")
    fun setHttpHeaders(table: DataTable) {
        val map = table.asMap()

        map.forEach { (key, value) -> headers.add(key, cleanEntry(value, variables)) }
    }

    @When("multipart request is performed")
    fun performMultipartFileRequest() {
        headers.clean().addTokenSession().addFormDataContentType()

        val response =
            restTemplate.postForEntity(
                requireNotNull(variables[REQUEST_URL]),
                HttpEntity(formDataBodyRequest, headers),
                Void::class.java
            )

        variables[HTTP_STATUS_CODE] = response.statusCodeValue.toString()
        variables.remove(REQUEST_BODY)
    }

    @When("request is performed")
    fun performRequest() {
        headers.clean().addTokenSession().addSubmissionType().apply { contentType = TEXT_PLAIN }
        val httpEntity = HttpEntity(variables[REQUEST_BODY], headers)

        val response =
            restTemplate.postForEntity(requireNotNull(variables[REQUEST_URL]), httpEntity, String::class.java)

        variables[HTTP_STATUS_CODE] = response.statusCodeValue.toString()
        variables[RESPONSE_BODY] = requireNotNull(response.body)
    }

    @Then("http status code {string} is returned with body:")
    fun getHttpStatusCodeAndBodyResponse(statusCode: String, body: String) {
        assertThat(variables[HTTP_STATUS_CODE]).isEqualTo(statusCode)
        assertThat(variables[RESPONSE_BODY]?.trim()).isEqualTo(body.trim())
    }
    @And("the file {string} contains:")
    fun assertTheFileContains(fileName: String, content: String) {
    }
    private fun HttpHeaders.addTokenSession(): HttpHeaders {
        headers.add(TOKEN_SESSION_ID_HEADER, variables[TOKEN_SESSION_ID])
        return this
    }

    private fun HttpHeaders.addFormDataContentType(): HttpHeaders {
        this.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        return this
    }

    private fun HttpHeaders.addPlainTextContentType(): HttpHeaders {
        this.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        return this
    }

    private fun HttpHeaders.addSubmissionType(): HttpHeaders {
        this.add(SUBMISSION_TYPE_HEADER, TEXT_PLAIN_VALUE)
        return this
    }

    private fun HttpHeaders.clean(): HttpHeaders {
        this.clear()
        return this
    }
}
