package com.panopset.compat

import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level

class LogEntry(val alert: AlertColor, val level: Level, val message: String) {
    val timestamp = Date()

    override fun toString(): String {
        return "$message $alert ${timestampFormat.format(timestamp)}"
    }
}

val timestampFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ~ ")
