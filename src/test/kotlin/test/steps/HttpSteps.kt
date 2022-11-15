package test.steps

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import test.common.*

class HttpSteps {
    private val restTemplate = SubmitFeatureContext.restTemplate
    private val variables = SubmitFeatureContext.variables
    private val headers = SubmitFeatureContext.headers
    private var formDataBodyRequest = SubmitFeatureContext.formDataBodyRequest
    private val tempFile = SubmitFeatureContext.tempFile

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
        val httpEntity = HttpEntity(variables[REQUEST_BODY], headers)

        val response =
            restTemplate.postForEntity(requireNotNull(variables[REQUEST_URL]), httpEntity, String::class.java)

        variables[HTTP_STATUS_CODE] = response.statusCodeValue.toString()
        variables[RESPONSE_BODY] = requireNotNull(response.body)
    }

    @When("request is performed")
    fun performRequest() {
        val httpEntity = HttpEntity(variables[REQUEST_BODY], headers)

        val response =
            restTemplate.postForEntity(requireNotNull(variables[REQUEST_URL]), httpEntity, String::class.java)

        variables[HTTP_STATUS_CODE] = response.statusCodeValue.toString()
        variables[RESPONSE_BODY] = requireNotNull(response.body)
    }

    @When("multipart request is performed")
    fun performMultipartFileRequest() {
        val response =
            restTemplate.postForEntity(
                requireNotNull(variables[REQUEST_URL]),
                HttpEntity(formDataBodyRequest, headers),
                Void::class.java
            )

        variables[HTTP_STATUS_CODE] = response.statusCodeValue.toString()
        variables.remove(REQUEST_BODY)
    }

    @And("a http request with form-data body:")
    fun setBodyInFormData(bodyTable: DataTable) {
        val map = bodyTable.asMap()
        val file = tempFile.resolve(requireNotNull(map["files"]).removePrefix("$")).canonicalFile

        formDataBodyRequest.add("files", FileSystemResource(file))
    }

    @Then("http status code {string} is returned")
    fun getHttpCode(statusCode: String) {
        assertThat(variables[HTTP_STATUS_CODE]).isEqualTo(statusCode)
    }


    @And("header(s)")
    fun setHttpHeaders(table: DataTable) {
        headers.clear()
        val map = table.asMap()

        map.forEach { (key, value) -> headers.add(key, cleanEntry(value, variables)) }
    }

    @Then("http status code {string} is returned with body:")
    fun getHttpStatusCodeAndBodyResponse(statusCode: String, body: String) {
        assertThat(variables[HTTP_STATUS_CODE]).isEqualTo(statusCode)
        assertThat(variables[RESPONSE_BODY]?.trim()).isEqualTo(body.trim())
    }

    private companion object {
        const val REQUEST_BODY = "bodyRequest"
        const val HTTP_METHOD = "httpMethod"
        const val REQUEST_URL = "urlPath"
        const val HTTP_STATUS_CODE = "httpStatusCode"
        const val RESPONSE_BODY = "bodyResponse"
    }
}
