package test.steps

import com.jayway.jsonpath.JsonPath
import io.cucumber.java.en.And
import test.common.SubmitFeatureContext

class JSONSteps {
    @And("is extracted from http response the JSONPath value {string} and saved into {string}")
    fun fromHttpResponseIsExtractByJSONPathAndSaveInVariableNamed(jsonPath: String, name: String) {
        val value = JsonPath.read<String>(SubmitFeatureContext.responseBody, jsonPath)

        SubmitFeatureContext.variables[TOKEN_SESSION_ID] = value
    }

    private companion object {
        const val TOKEN_SESSION_ID = "token"
    }
}
