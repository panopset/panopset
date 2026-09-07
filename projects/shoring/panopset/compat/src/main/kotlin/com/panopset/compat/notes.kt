package com.panopset.compat

import com.panopset.compat.Stringop.getEol
import java.io.File
import java.io.StringWriter
import java.util.logging.Level

// From FontManagerFX.kt
//fun getCurrentBaseStyle(): String {
//    return getStyleFor(fontSize.value)
//}

// From FxLogFiler.kt
//fun updateLogFile(fxDoc: FxDoc, msg: String) {
//    val logPanHistory = findLogpan(fxDoc).getEntryStackAsTextlg()
//    appendToLogFile(fxDoc, logPanHistory)
//}

// From Logpan
//fun debuglg(msg: String?) {
//    reportlg(
//        LogEntry(
//            AlertColor.ORG, Level.FINE,
//            auditlg(msg)!!
//        )
//    )
//}
//fun warnlg(ex: Exception) {
//    warnlg(ex.message?:"")
//}
//fun errorMsglg(msg: String, ex: Throwable) {
//    errorMsglg(msg)
//    handleExceptionlg(ex)
//}
//
//private fun greenlg(msg: String?) {
//    dspmsglg(msg)
//}
//
//private fun handlelg(e: Exception?) {
//    handleExceptionlg(e!!)
//}
//
//fun errorMsglg(file: File, ex: Throwable) {
//    infolg(Fileop.getCanonicalPath(file))
//    errorExlg(ex)
//}
//fun getEntryStackAsTextlg(): String {
//    return printHistorylg()
//}
//
//fun clearlg() {
//    stacklg.clear()
//}
//private fun printHistorylg(): String {
//    val sw = StringWriter()
//    for (lr in stacklg) {
//        sw.append(timestampFormat.format(lr.timestamp))
//        sw.append(lr.message)
//        sw.append("\n")
//    }
//    return sw.toString()
//}
//fun errorExlg(ex: Throwable) {
//    handleExceptionlg(ex)
//}
//private fun handleExceptionlg(ex: Throwable) {
//    logger.log(Level.SEVERE, ex.message, ex)
//    val logEntry = LogEntry(
//        AlertColor.RED, Level.SEVERE,
//        ex.message ?: standardWierdErrorMessage
//    )
//    logaloglg(logEntry)
//}
//private val clearLogEntry = LogEntry(AlertColor.GRN, Level.INFO, "")
//fun warnlg(msg: String) {
//    reportlg(LogEntry(AlertColor.YLW, Level.WARNING,msg))
//}
// From Logpan.kt
//fun getFileErrorMessage(message: String?, file: File?): String {
//    if (file == null) {
//        return "Null file."
//    }
//    return "$message: ${Fileop.getCanonicalPath(file)}"
//}
//fun getStackTraceAndCauseslg(throwable: Throwable): String {
//    val sw = StringWriter()
//    sw.append("See log")
//    sw.append(": ")
//    sw.append(throwable.message)
//    sw.append(getEol())
//    sw.append("*************************")
//    sw.append(getEol())
//    sw.append(getStackTracelg(throwable))
//    sw.append(getEol())
//    var cause = throwable.cause
//    while (cause != null) {
//        sw.append("*************************")
//        sw.append(getEol())
//        sw.append(getStackTracelg(cause))
//        sw.append(getEol())
//        cause = cause.cause
//    }
//    return sw.toString()
//}

// from SteelWrapper
//
//fun findLogpan(fxDoc: FxDoc): Logpan {
//    return findLogStageDisplayElements(fxDoc).logpan
//}
