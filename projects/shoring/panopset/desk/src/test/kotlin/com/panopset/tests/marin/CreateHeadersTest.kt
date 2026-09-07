package com.panopset.tests.marin

import com.panopset.desk.utilities.createHeaders
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CreateHeadersTest {

    @Test
    fun test() {
        val headers = createHeaders(rawHeaders)
        Assertions.assertEquals(10, headers.size)
        Assertions.assertEquals( "*/*", headers["Accept"])
        Assertions.assertEquals("Basic asdf", headers["Authorization"])
        Assertions.assertEquals("PF=asdf", headers["Cookie"])
    }
}


const val rawHeaders = "Content-Type: application/x-www-form-urlencoded\n" +
        "Authorization: Basic asdf\n" +
        "User-Agent: PostmanRuntime/7.36.1\n" +
        "Accept: */*\n" +
        "Cache-Control: no-cache\n" +
        "Host: panopset.com\n" +
        "Accept-Encoding: gzip, deflate, br\n" +
        "Connection: keep-alive\n" +
        "Content-Length: 84\n" +
        "Cookie: PF=asdf"
