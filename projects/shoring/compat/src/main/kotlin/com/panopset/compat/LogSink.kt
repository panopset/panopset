package com.panopset.compat

import java.io.File

interface LogSink {
    fun clear()
    fun green(msg: String)
    fun yellow(msg: String)
    fun red(msg: String, throwable: Throwable)
    fun red(msg: String)
    fun red(throwable: Throwable)
    fun red(msg: String, file: File, throwable: Throwable) {
        val filePath = file.absolutePath
        red("$msg\n$filePath", throwable)
    }
    fun errorMsg(file: File, throwable: Throwable) {
        red(throwable.message ?: "", file, throwable)
    }
    fun errorMsg(msg: String) {
        red(msg)
    }
    fun warn(msg: String) {
        yellow(msg)
    }
    fun errorEx(ex: Throwable) {
        red(ex)
    }
    fun errorMsg(msg: String, file: File) {
        val filePath = file.absolutePath
        red("$msg\n$filePath")
    }
    fun errorMsg(msg: String, throwable: Throwable) {
        red(msg, throwable)
    }
    fun debug(msg: String) {
    }
    fun info(msg: String) {
        green(msg)
    }
    fun dspmsg(msg: String) {
        green("$msg\n")
    }
}
