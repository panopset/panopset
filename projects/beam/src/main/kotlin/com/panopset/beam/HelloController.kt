package com.panopset.beam

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class VersionController {
    @GetMapping("/hello")
    fun hello(@RequestParam(value = "name", defaultValue = "bar") name: String): String {
        return String.format("Hello %s!", name)
    }
}
