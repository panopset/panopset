package com.panopset.desk.utilities.lowerclass

import com.panopset.compat.Logz
import com.panopset.compat.StatusListener
import com.panopset.compat.Stringop
import java.io.File
import java.io.IOException

class VersionParser() : StatusListener {

    constructor(jarOrDirectoryToTraverse: File): this() {
        file = jarOrDirectoryToTraverse
    }

    private var mavenHome: String = ""
        get() {
            if (field.isEmpty()) {
                field = System.getenv()["M2_HOME"]?:""
                if (field.isEmpty()) {
                    field = System.getenv()["MAVEN_HOME"]?:""
                }
                if (field.isEmpty()) {
                    field = DEFAULT_MAVEN_HOME
                }
            }
            return field
        }

    private var file: File = File(mavenHome)

    var summaryReport: String = ""
        get() {
            if (field.isEmpty()) {
                field = createReport(false)
            }
            return field
        }
        private set

    private var detailReport: String? = null

    val detailedReport: String?
        get() {
            if (detailReport == null) {
                detailReport = createReport(true)
            }
            return detailReport
        }

    private fun createReport(printDetails: Boolean): String {
        val vm = VersionMakeup()
        vm.analyze(file, printDetails)
        return vm.report
    }

    companion object {
        var DEFAULT_MAVEN_HOME: String = "${Stringop.USH}/.m2"

        @Throws(IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            Logz.dspmsg("*** Entire repository example:")
            Logz.dspmsg(VersionParser().summaryReport)
        }
    }

    override fun update(message: String) {
        Logz.dspmsg(message)
    }
}
