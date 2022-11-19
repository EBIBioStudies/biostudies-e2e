package test.steps

import com.jayway.jsonpath.JsonPath
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import test.common.SubmitFeatureContext.cleanAndReplaceString
import test.common.SubmitFeatureContext.createFormData
import test.common.SubmitFeatureContext.variables

class HttpSteps {
    private val restTemplate = RestTemplate()
    private val headers = HttpHeaders()
    private lateinit var responseBody: String
    private lateinit var bodyRequest: String
    private lateinit var urlPath: String
    private var formDataBodyRequest = LinkedMultiValueMap<String, Any>()
    private lateinit var httpMethod: HttpMethod
    private lateinit var httpStatusCode: String

    @Given("a http request with body:")
    fun defineBodyRequest(body: String) {
        bodyRequest = cleanAndReplaceString(body)
    }

    @And("url path {string}")
    fun setUrlPath(path: String) {
        urlPath = cleanAndReplaceString(path)
    }

    @And("http method {string}")
    fun setHttpMethod(method: String) {
        fun stringToHttpMethod(method: String): HttpMethod {
            return when (method) {
                "GET" -> HttpMethod.GET
                "POST" -> HttpMethod.POST
                "PUT" -> HttpMethod.PUT
                "DELETE" -> HttpMethod.DELETE
                else -> throw Exception("Http method \"${method}\" not found")
            }
        }
        httpMethod = stringToHttpMethod(method)
    }

    @When("json request is performed")
    fun performJsonRequest() {
        val response = restTemplate.postForEntity(urlPath, HttpEntity(bodyRequest, headers), String::class.java)

        httpStatusCode = response.statusCodeValue.toString()
        responseBody = requireNotNull(response.body)
    }

    @When("request is performed")
    fun performRequest() {
        val response = restTemplate.postForEntity(urlPath, HttpEntity(bodyRequest, headers), String::class.java)

        httpStatusCode = response.statusCodeValue.toString()
        responseBody = requireNotNull(response.body)
    }

    @When("multipart request is performed")
    fun performMultipartFileRequest() {
        val response = restTemplate.postForEntity(urlPath, HttpEntity(formDataBodyRequest, headers), Void::class.java)

        httpStatusCode = response.statusCodeValue.toString()
    }

    @And("a http request with form-data body:")
    fun setBodyInFormData(bodyTable: DataTable) {
        val map = bodyTable.asMap()

        createFormData(map).forEach { formDataBodyRequest.add(it.key, it.value) }
    }

    @Then("http status code {string} is returned")
    fun getHttpCode(statusCode: String) {
        assertThat(httpStatusCode).isEqualTo(statusCode)
    }
    @Then("http status code {string} is returned and taken from response the JSONPath value {string} and saved into {string}")
    fun getHttpCode2(statusCode: String,jsonPath: String, name: String) {
        assertThat(httpStatusCode).isEqualTo(statusCode)

        val value = JsonPath.read<String>(responseBody, jsonPath)

        variables[TOKEN_SESSION_ID] = value
    }

    @And("header(s)")
    fun setHttpHeaders(table: DataTable) {
        headers.clear()
        val map = table.asMap()

        map.forEach { (key, value) -> headers.add(key, cleanAndReplaceString(value)) }
    }

    @Then("http status code {string} is returned with body:")
    fun getHttpStatusCodeAndBodyResponse(statusCode: String, body: String) {
        assertThat(httpStatusCode).isEqualTo(statusCode)
        assertThat(responseBody).isEqualTo(body)
    }

    private companion object {
        const val TOKEN_SESSION_ID = "token"
    }
}
