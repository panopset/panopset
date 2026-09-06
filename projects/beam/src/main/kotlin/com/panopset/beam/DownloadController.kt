package com.panopset.beam

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URLDecoder


@Controller
class DownloadController {

    @GetMapping("/hd")
    @Throws(IOException::class)
    fun download(@RequestBody hdForm: HdForm ): ResponseEntity<Any> {
        if ("email@something.com" != URLDecoder.decode(hdForm.pw.lowercase(), java.nio.charset.StandardCharsets.UTF_8.toString())) {
            return ResponseEntity<Any>(HttpStatus.BAD_REQUEST)
        }
        val file = File("/somefile")
        val headers: HttpHeaders = HttpHeaders()
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate")
        headers.add("Pragma", "no-cache")
        headers.add("Expires", "0")
        val resource: InputStreamResource = InputStreamResource(FileInputStream(file))
        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(file.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }
}

class HdForm {
    var pw = ""
}
