package com.panopset.compat

import com.panopset.compat.Stringop.getEol
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.logging.Level
import java.util.logging.Logger

private val logger: Logger = Logger.getGlobal()
private val clearLogEntry = LogEntry(AlertColor.GRN, Level.INFO, "")
const val standardWierdErrorMessage =
    "Unexpected error, if your pull request is accepted, we'll send you 1000 currently worthless Panopset shares."


object Logpan {
    /**
     * Keeping this around, because of the System/38 &amp; AS/400 DSPMSG CLP command. Identical to
     * info(msg).
     *
     * @param msg Message to log at Level.INFO.
     */
    fun dspmsglg(msg: String?) {
        infolg(msg)
    }

    fun infolg(msg: String?) {
        reportlg(
            LogEntry(
                AlertColor.GRN, Level.INFO,
                auditlg(msg)!!
            )
        )
    }

    fun warnlg(msg: String?) {
        reportlg(
            LogEntry(
                AlertColor.YLW, Level.WARNING,
                auditlg(msg)!!
            )
        )
    }

    fun debuglg(msg: String?) {
        reportlg(
            LogEntry(
                AlertColor.ORG, Level.FINE,
                auditlg(msg)!!
            )
        )
    }

    fun errorMsglg(msg: String?) {
        reportlg(
            LogEntry(
                AlertColor.RED, Level.SEVERE,
                auditlg(msg)!!
            )
        )
    }

    private fun auditlg(msg: String?): String? {
        return msg
    }

    fun warnlg(ex: Exception) {
        warnlg(ex.message)
    }

    fun errorExlg(ex: Throwable) {
        handleExceptionlg(ex)
    }

    fun errorMsglg(msg: String, ex: Throwable) {
        errorMsglg(msg)
        handleExceptionlg(ex)
    }

    private fun greenlg(msg: String?) {
        dspmsglg(msg)
    }

    private fun handlelg(e: Exception?) {
        handleExceptionlg(e!!)
    }

    fun errorMsglg(message: String?, file: File?) {
        if (file == null) {
            errorMsglg("Null file.")
            return
        }
        errorMsglg("$message: ${Fileop.getCanonicalPath(file)}")
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

    fun errorMsglg(file: File, ex: Throwable) {
        infolg(Fileop.getCanonicalPath(file))
        errorExlg(ex)
    }

    private fun handleExceptionlg(ex: Throwable) {
        logger.log(Level.SEVERE, ex.message, ex)
        val logEntry = LogEntry(
            AlertColor.RED, Level.SEVERE,
            ex.message ?: standardWierdErrorMessage
        )
        logaloglg(logEntry)
    }

    private fun reportlg(logRecord: LogEntry) {
        logger.log(logRecord.level, logRecord.message)
        logaloglg(logRecord)
    }

//  public static String logAndReturnError(String msg) {
//    Logop.errorMsg(msg);
//    return msg;
//  }
//  public static String logAndReturnExceptionError(Exception ex) {
//    String rtn = ex.getMessage();
//    if (!Stringop.isPopulated(ex.getMessage())) {
//      rtn = PAN_STANDARD_LOGIC_ERROR_MSG;
//    }
//    errorMsg(rtn);
//    return rtn;
//  }
//  public static String logAndReturnError(String msg) {

    fun getEntryStackAsTextlg(): String {
        return printHistorylg()
    }

    fun clearlg() {
        stacklg.clear()
    }

    val stacklg: Deque<LogEntry> = ConcurrentLinkedDeque()
    private fun printHistorylg(): String {
        val sw = StringWriter()
        for (lr in stacklg) {
            sw.append(timestampFormat.format(lr.timestamp))
            sw.append(lr.message)
            sw.append("\n")
        }
        return sw.toString()
    }

    private fun logaloglg(logEntry: LogEntry) {
        if (stacklg.size > 10) {
            stacklg.removeLast()
        }
        stacklg.push(logEntry)
    }

    fun getStackTraceAndCauseslg(throwable: Throwable): String {
        val sw = StringWriter()
        sw.append("See log")
        sw.append(": ")
        sw.append(throwable.message)
        sw.append(getEol())
        sw.append("*************************")
        sw.append(getEol())
        sw.append(getStackTracelg(throwable))
        sw.append(getEol())
        var cause = throwable.cause
        while (cause != null) {
            sw.append("*************************")
            sw.append(getEol())
            sw.append(getStackTracelg(cause))
            sw.append(getEol())
            cause = cause.cause
        }
        return sw.toString()
    }
}