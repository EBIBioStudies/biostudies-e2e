package test.common

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

fun cleanEntry(entry: String, variables: Map<String, String>): String {
    var result = entry
    variables.forEach { (key, value) -> result = result.replace("\$${key}", value) }
    return result
}

const val REQUEST_BODY = "bodyRequest"
const val REQUEST_URL = "urlPath"
const val HTTP_METHOD = "httpMethod"
const val HTTP_STATUS_CODE = "httpStatusCode"
const val RESPONSE_BODY = "bodyResponse"
const val TOKEN_SESSION_ID = "token"
const val TOKEN_SESSION_ID_HEADER = "X-Session-Token"
const val SUBMISSION_TYPE_HEADER = "Submission_Type"
