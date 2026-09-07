package com.panopset.compat

var timeout = 5000

fun doGetHttp(urlStr: String): HttpResponsePackage {
    return doGetHttp(HashMap(), urlStr)
}

fun doGetHttp(headers: MutableMap<String, String>, urlStr: String): HttpResponsePackage {
    return doHttp(headers, urlStr, "")
}

private fun doHttp(headers: MutableMap<String, String>, urlStr: String, postData: String): HttpResponsePackage {
    val httpComm: HttpComm = HttpCommFactory(urlStr)
        .withHeaders(headers)
        .withPostData(postData)
        .assembleHttpComm()
    val httpResponsePackage: HttpResponsePackage = httpComm.exec()
    return httpResponsePackage
}

fun doPostHttp(headers: MutableMap<String, String>, urlStr: String, postData: String): HttpResponsePackage {
    return doHttp(headers, urlStr, postData)
}

fun constrain(s: String): String {
    return "${s.substring(0, 20)} \n\n Response was too big to handle."
}
