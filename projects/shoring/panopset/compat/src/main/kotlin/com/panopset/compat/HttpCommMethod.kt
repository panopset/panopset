package com.panopset.compat

import java.net.HttpURLConnection

enum class HttpCommMethod {
    GET {
        override fun panopset(httpURLConnection: HttpURLConnection,
                              headers: HashMap<String, String>) {
            addHeaderIfNotThereAlready(headers, CONTENT_TYPE,
                "text/html; charset=utf-8")
        }
    },

    POST {
        override fun panopset(httpURLConnection: HttpURLConnection,
                              headers: HashMap<String, String>) {
            httpURLConnection.requestMethod = POST.name
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = true
            httpURLConnection.useCaches = false
            addHeaderIfNotThereAlready(headers, USER_AGENT,
                "Mozilla/4.0")
            addHeaderIfNotThereAlready(headers, CONTENT_TYPE,
                "application/json")
        }
    };

    abstract fun panopset(
        httpURLConnection: HttpURLConnection,
        headers: HashMap<String, String>)
}

private fun addHeaderIfNotThereAlready(headers: HashMap<String, String>,
                                       key: String, value: String) {
    if (!headers.containsKey(key)) {
        headers[key] = value
    }
}

private const val USER_AGENT = "User-Agent"
private const val CONTENT_TYPE = "Content-Type"
private const val EMPTY_URL_MSG = "No URL provided, nothing to do."
