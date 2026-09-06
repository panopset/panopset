package com.panopset.compat

import java.io.File

interface LogDisplayer {
    fun info(msg: String) {
        dspmsg(msg)
    }
    fun dspmsg(msg: String)
    fun warn(msg: String)
    fun clear()
    fun getPriorMessage(): String
    fun green(msg: String)
    fun errorMsg(msg: String, throwable: Throwable)
    fun errorMsg(msg: String, file: File)
    fun errorMsg(msg: String)
    fun errorMsg(file: File, throwable: Throwable)
    fun errorEx(throwable: Throwable)
    fun debug(msg: String)
}
