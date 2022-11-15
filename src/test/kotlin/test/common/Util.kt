package test.common

fun cleanEntry(entry: String, variables: Map<String, String>): String {
    var result = entry
    variables.forEach { (key, value) -> result = result.replace("\$${key}", value) }
    return result
}
