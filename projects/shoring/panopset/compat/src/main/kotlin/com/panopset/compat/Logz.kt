package com.panopset.compat

import java.io.PrintWriter
import java.io.StringWriter
import java.util.logging.Level
import java.util.logging.Logger

object Logz: LogSink {

    var logzDsiplayer: LogDisplayer = LogzDisplayerCMD

    private val logger: Logger = Logger.getGlobal()
    private val appLoggers = mutableListOf<LogSink>()

    fun setAppLogger(logger: LogSink) {
        this.appLoggers.clear()
        this.appLoggers.add(logger)
    }

    override fun green(msg: String) {
        logger.log(Level.INFO, msg)
        if (appLoggers.isNotEmpty()) {
            appLoggers.forEach { it.green(msg) }
        }
    }

    override fun yellow(msg: String) {
        logger.log(Level.WARNING, msg)
        if (appLoggers.isNotEmpty()) {
            appLoggers.forEach { it.yellow(msg) }
        }
    }

    override fun red(msg: String, throwable: Throwable) {
        logger.log(Level.SEVERE, msg, throwable)
        if (appLoggers.isNotEmpty()) {
            appLoggers.forEach { it.red(msg, throwable) }
        }
    }

    override fun red(msg: String) {
        logger.log(Level.SEVERE, msg)
        if (appLoggers.isNotEmpty()) {
            appLoggers.forEach { it.red(msg) }
        }
    }

    override fun red(throwable: Throwable) {
        logger.log(Level.SEVERE, throwable.message, throwable)
        if (appLoggers.isNotEmpty()) {
            appLoggers.forEach { it.red(throwable) }
        }
    }

    override fun debug(msg: String) {
        logger.log(Level.FINE, msg)
    }

    override fun clear() {
        if (appLoggers.isNotEmpty()) {
            appLoggers.forEach { it.clear() }
        }
    }
    fun getPriorMessage(): String {
        return logzDsiplayer.getPriorMessage()
    }

}

fun getStackTracelg(throwable: Throwable): String {
    StringWriter().use { sw ->
        PrintWriter(sw).use { pw ->
            throwable.printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }
    }
}
