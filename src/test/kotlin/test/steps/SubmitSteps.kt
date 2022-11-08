package test.steps

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import test.common.JsonMapper.Companion.createMapper
import test.common.LoginUser
import test.common.toHttpMethod
import test.steps.Stage.LOGIN

enum class Stage {
    LOGIN, UPLOAD_FILE, SUBMIT
}

class SubmitSteps {
    lateinit var urlEnvironment: String
    lateinit var ftpUrl: String
    lateinit var userName: String
    lateinit var password: String

    val restTemplate = RestTemplate()
    val jsonMapper = createMapper()
    lateinit var loginObject: LoginUser

    lateinit var requestPath: String
    lateinit var httpMethod: HttpMethod
    var stage: Stage = LOGIN
    val headers: HttpHeaders = HttpHeaders()


    @Given("the setup information")
    fun theSetupInformation(table: DataTable) {
        val something: List<Map<String, String>> = table.asmap(String::class.java, String::class.java)
        urlEnvironment = something.first()["environmentUrl"] ?: "defaultEnvironmentUrl"
        ftpUrl = something.first()["ftpUrl"] ?: "defaultFtpUrl"
        userName = something.first()["userName"] ?: "defaultUserName"
        password = something.first()["userPassword"] ?: "defaultUserPassword"
    }

    @Given("a http request with body:")
    fun defineBodyRequest(body: String) {
        if (stage == LOGIN) {
            val cleanedBody = body.replace("\$userName", userName).replace("\$userPassword", password)
            loginObject = jsonMapper.readValue(cleanedBody, LoginUser::class.java)
        }

    }

    @And("url path {string}")
    fun setUrlPath(path: String) {
        requestPath = path.replace("\$environmentUrl", urlEnvironment)
    }

    @And("http method {string}")
    fun setHttpMethod(method: String) {
        httpMethod = method.toHttpMethod()
    }

    @When("request is performed")
    fun performRequest() {
        val response = restTemplate.postForEntity(urlEnvironment, loginObject, String::class.java)

    }

    @Then("http status code {string} is returned")
    fun getHttpCode(statusCode: String) {
    }

    @And("http response the JSONPath value {string} is saved into {string}")
    fun fromHttpResponseIsExtractByJSONPathAndSaveInVariableNamed(value: String, name: String) {
    }

    @And("header(s)")
    fun setHttpHeaders(table: DataTable) {
        val list = table.asMaps().forEach { headers.set(it["key"].toString(), it["value"]) }
    }

    @And("a http request with form-data body:")
    fun setBodyInFormData(body: DataTable) {
    }

    @Then("http status code {string} is returned with body:")
    fun getHttpStatusCodeAndBodyResponse(statusCode: String, body: String) {
    }

    @And("the file {string} with content")
    fun createFileWithContent(fileName: String, content: String) {
    }

    @And("the file {string} contains:")
    fun assertTheFileContains(fileName: String, content: String) {
    }
}
