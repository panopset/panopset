package com.panopset.compat

import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.ObjectWriter

class Jsonop<T> {
    fun toJson(obj: T): String {
        val ow: ObjectWriter = ObjectMapper().writer().withDefaultPrettyPrinter()
        val json: String = ow.writeValueAsString(obj)
        return json
    }

    fun fromJson(rawJson: String, clazz: Class<T>): T {
        return ObjectMapper().readValue(rawJson, clazz)
    }
}
