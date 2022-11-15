package test.steps

import com.jayway.jsonpath.JsonPath
import io.cucumber.java.en.And
import test.common.SubmitFeatureContext

class JSONSteps {
    private val variables = SubmitFeatureContext.variables

    @And("is extracted from http response the JSONPath value {string} and saved into {string}")
    fun fromHttpResponseIsExtractByJSONPathAndSaveInVariableNamed(jsonPath: String, name: String) {
        val value = JsonPath.read<String>(variables[RESPONSE_BODY], jsonPath)

        variables[TOKEN_SESSION_ID] = value
    }

    private companion object {
        const val RESPONSE_BODY = "bodyResponse"
        const val TOKEN_SESSION_ID = "token"
    }
}
