package test.common

import org.springframework.http.HttpMethod

fun String.toHttpMethod(): HttpMethod {
    return when (this) {
        "POST" -> HttpMethod.POST
        "GET" -> HttpMethod.GET
        "PUT" -> HttpMethod.PUT
        "DELETE" -> HttpMethod.DELETE
        else ->  throw Exception("Http request not found")
    }
}