package com.panopset.compat

class HttpCommFactory(urlStr: String) {
    private val httpComm = HttpComm(urlStr)

    fun assembleHttpComm(): HttpComm {
        httpComm.preassemble()
        return httpComm
    }

    fun withHeaders(headers: MutableMap<String, String>): HttpCommFactory {
        for (e in headers.entries) {
            addHeader(e.key, e.value)
        }
        return this
    }

    fun withPostData(postData: String): HttpCommFactory {
        httpComm.postData = postData
        return this
    }

    private fun addHeader(key: String, value: String) {
        httpComm.addHeader(key, value)
    }
}
