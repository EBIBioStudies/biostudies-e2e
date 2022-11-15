package test.steps

import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import test.common.SubmitFeatureContext

class SetupSteps {
    private val variables = SubmitFeatureContext.variables

    @Given("the setup information")
    fun theSetupInformation(table: DataTable) {
        variables.putAll(table.asMap())
    }
}
