package test.common

object ContextVariables {
    private val variables = mutableMapOf<String, Any>()

    operator fun get(key: String): Any {
        return requireNotNull(variables[key])
    }

    operator fun set(key: String, value: Any) {
        variables[key] = value
    }

    fun setAll(map: Map<String, Any>) {
        variables.putAll(map)
    }

    fun cleanAndReplaceString(entry: String): String {
        var result = entry
        val stringVariable = variables.filter { it.value is String } as Map<String, String>
        stringVariable.forEach { (key, value) -> result = result.replace("\$${key}", value) }
        return result
    }
}
