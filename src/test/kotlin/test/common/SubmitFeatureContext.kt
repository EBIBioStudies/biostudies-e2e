package test.common

object SubmitFeatureContext {
    val variables = mutableMapOf<String, Any>()
    lateinit var responseBody: String

    fun cleanAndReplaceString(entry: String): String {
        var result = entry
        val stringVariable = variables.filter { it.value is String } as Map<String, String>
        stringVariable.forEach { (key, value) -> result = result.replace("\$${key}", value) }
        return result
    }

    fun createFormData(map: Map<String, String>): Map<String, Any> {
        return map.mapValues { variables[cleanString(it.value)] }.filterValues { it != null } as Map<String, Any>
    }
    fun cleanString(entry: String): String = entry.replace("$", "")
}
