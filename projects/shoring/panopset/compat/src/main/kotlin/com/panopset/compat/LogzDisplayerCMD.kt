package com.panopset.compat

import java.io.File

object LogzDisplayerCMD : LogDisplayer {
    override fun dspmsg(msg: String) {
        Logpan.dspmsglg(msg)
    }

    override fun warn(msg: String) {
        Logpan.warnlg(msg)
    }

    override fun debug(msg: String) {
        Logpan.debuglg(msg)
    }

    override fun errorMsg(msg: String, throwable: Throwable) {
        Logpan.errorMsglg(msg, throwable)
    }

    override fun errorMsg(msg: String, file: File) {
        Logpan.errorMsglg(msg, file)
    }

    override fun errorMsg(msg: String) {
        Logpan.errorMsglg(msg)
    }

    override fun errorMsg(file: File, throwable: Throwable) {
        Logpan.errorMsglg(file, throwable)
    }

    override fun errorEx(throwable: Throwable) {
        Logpan.errorExlg(throwable)
    }

    override fun getPriorMessage(): String {
        return Logpan.stacklg.peek().message
    }

    override fun green(msg: String) {
        dspmsg(msg)
    }

    override fun clear() {
        Logpan.stacklg.clear()
    }
}
