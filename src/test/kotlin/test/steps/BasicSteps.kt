package test.steps

import io.cucumber.java.en.Given
import test.common.ContextVariables

class BasicSteps {
    @Given("the variable {string} with value")
    fun setStringVariableUsingMut(variableName: String, value: String) {
        ContextVariables[variableName] = value
    }

    @Given("the variable {string} with value {string}")
    fun setStringVariable(variableName: String, value: String) {
        ContextVariables[variableName] = value
    }
}
