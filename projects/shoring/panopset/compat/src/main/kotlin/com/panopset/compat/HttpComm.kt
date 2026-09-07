package com.panopset.compat

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.StringWriter
import java.net.HttpURLConnection
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class HttpComm(
    private val urlStr: String
) {
    private var isUrlEncoded: Boolean = false
    private val headers = HashMap<String, String>()
    private var method: HttpCommMethod = HttpCommMethod.GET
    var postData = ""

    fun exec(): HttpResponsePackage {
        val conn = constructConnection()
        conn.requestMethod = method.name
        if (postData.isNotEmpty()) {
            if (method != HttpCommMethod.POST) {
                throw RuntimeException("Should be a POST.")
            }
            val osw = OutputStreamWriter(conn.outputStream)
            val finalPostData = if (isUrlEncoded) {
                URLEncoder.encode(postData, StandardCharsets.UTF_8.name())
            } else {
                postData
            }
            osw.write(finalPostData)
            osw.flush()
        }
        val rtn = readResponseFromConnection(conn)
        return rtn
    }

    private fun constructConnection(): HttpURLConnection {
        val url = URI(urlStr).toURL()
        val rtn = url.openConnection() as HttpURLConnection
        rtn.connectTimeout = timeout
        for (e in headers.entries) {
            if (e.key == "Content-Type" && e.value == "application/x-www-form-urlencoded") {
                isUrlEncoded = true
            }
            rtn.setRequestProperty(e.key, e.value);
        }
        method.panopset(rtn, headers)
        return rtn
    }

    fun addHeader(key: String, value: String) {
        if (headers.contains((key))) {
            Logz.warn("Replacing ${key}:${this.headers[value]} with ${headers[key]}!")
        }
        headers[key] = value
    }

    private fun readResponseFromConnection(con: HttpURLConnection): HttpResponsePackage {
        val url = con.url ?: return createEmptyURLResponse()
        val urlStr = url.toString()
        if (urlStr.isEmpty()) return createEmptyURLResponse()
        var conResponseCode = -1
        try {
            val ins = con.inputStream
            val limit = Runtime.getRuntime().freeMemory() * .80
            conResponseCode = con.responseCode
            if (conResponseCode != 200) {
                return HttpResponsePackage(conResponseCode, "Error occurred.", con.responseMessage)
            }
            val isr = InputStreamReader(ins)
            val bfr = BufferedReader(isr)
            val sw = StringWriter()
            var s = bfr.readLine()
            var totalSize = 0
            while (s != null && (totalSize < limit)) {
                totalSize += s.length
                if (s.length > limit) {
                    return HttpResponsePackage(conResponseCode, constrain(s), "Exceeded 80% of available memory: $limit")
                }
                sw.append(s)
                sw.append("\n")
                s = bfr.readLine()
            }
            bfr.close()
            return HttpResponsePackage(conResponseCode, sw.toString())
        } catch (t: Throwable) {
            return createErrorResponse(conResponseCode, t, urlStr)
        } finally {
            con.disconnect()
        }
    }

    fun preassemble() {
        if (postData.isNotEmpty()) {
            method = HttpCommMethod.POST
        }
    }
}

private fun createEmptyURLResponse(): HttpResponsePackage {
    return createErrorResponse(400, "URL is empty")
}

private fun createErrorResponse(responseCode: Int, errorMessage: String): HttpResponsePackage {
    Logz.warn("$responseCode: $errorMessage")
    return HttpResponsePackage(responseCode, errorMessage)
}

fun createErrorResponse(responseCode: Int, throwable: Throwable, newURL: String): HttpResponsePackage {
    val errorMessage = "$responseCode: $newURL \n\n ${getStackTracelg(throwable)}"
    Logz.errorMsg(errorMessage)
    return HttpResponsePackage(responseCode, errorMessage)
}
