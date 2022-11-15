package test.steps

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import test.common.SubmitFeatureContext

class SetupSteps {
    @Given("the setup information")
    fun theSetupInformation(table: DataTable) {
        SubmitFeatureContext.variables.putAll(table.asMap())
    }
}
