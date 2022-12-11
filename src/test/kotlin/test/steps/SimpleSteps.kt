package test.steps

import io.cucumber.java.en.Given
import test.common.ContextVariables

class SimpleSteps {
    @Given("the variable {string} with value")
    fun setStringVariable(variableName: String, value: String) {
        ContextVariables[variableName] = value
    }
}
