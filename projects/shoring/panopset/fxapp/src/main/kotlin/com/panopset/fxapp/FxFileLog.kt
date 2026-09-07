package com.panopset.fxapp

import java.io.File

fun appendToLogFile(fxDoc: FxDoc, msg: String) {
    val logFile = getLogFile(fxDoc)
    logFile.appendText(msg)
}

fun clearLogFileContents(fxDoc: FxDoc) {
    val logFile = getLogFile(fxDoc)
    logFile.writeText("")
}

fun getLogFileContents(fxDoc: FxDoc): String {
    val logFile = getLogFile(fxDoc)
    return if (logFile.exists()) logFile.readText() else ""
}

private fun getLogFile(fxDoc: FxDoc): File {
    return File("${fxDoc.getTheFilePath()}.log")
}
