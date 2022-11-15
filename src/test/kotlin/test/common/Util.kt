package test.common

fun getVariable(variableName: String): Any {
    return requireNotNull(SubmitFeatureContext.variables[variableName])
}

fun cleanStringEntry(entry: String): String {
    var result = entry
    val stringVariable = SubmitFeatureContext.variables.filter { it.value is String } as Map<String, String>
    stringVariable.forEach { (key, value) -> result = result.replace("\$${key}", value) }
    return result
}
