package com.panopset.desk.utilities.dash.publish

import com.panopset.compat.AppVersion
import com.panopset.compat.Fileop
import com.panopset.compat.opSecureCopyGet
import com.panopset.compat.opSecureCopyPut
import com.panopset.compat.props2map
import com.panopset.desk.utilities.GenerateDownloadsTable
import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.flywheel.FlywheelBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class SiteDeploy(val gcs: GrandCentralStation) {
    val rhd = gcs.createRemoteHostData()
    val d = rhd.d

    fun publish() {
        val projectName = gcs.projectDir.name
        val projectBaseDir = gcs.projectDirectorySelector.createDir()
        val tmpDownloadsDir = File(Fileop.combinePaths(
            projectBaseDir,
            "/tmp/downloads"
        ))
        val tmpHtmlDir = File(Fileop.combinePaths(
            projectBaseDir,
            "/tmp/html"
        ))
        val fromScp = "/var/www/$d/html/downloads"
        opSecureCopyGet(
            rhd, tmpDownloadsDir, fromScp, arrayListOf("json")
        )
        val blurb = if (d == "$projectName.com") {
            ""
        } else {
            "<h1>Prototype</h1>$d is currently serving as a prototype for the next release of " +
                    "<a href=\"https://panopset.com\">panopset.com</a>."
        }
        FlywheelBuilder().file(
            gcs.createSlabTemplateDriverFile()
        )
            .targetDirectory(tmpHtmlDir)
            .map("previewBlurb", blurb)
            .map("downloadsTable", GenerateDownloadsTable(gcs).createDownloadsTable(tmpDownloadsDir.canonicalPath))
            .map("appVersion", AppVersion.getVersion())
            .map("fullVersion", AppVersion.getFullVersion())
            .map("dashDate", dashDateFormat.format(Date()))
            .map("timestamp", timestampFormat.format((Date())))
            .map(props2map(Fileop.loadProps(
                Fileop.combinePaths(
                    projectBaseDir,
                    "deploy.properties"
                )
            ))).construct().exec()
        opSecureCopyPut(rhd, tmpHtmlDir.absolutePath, "/var/www/$d/html")
    }
}

private val dashDateFormat = SimpleDateFormat("yyyy-MM-dd")
private val timestampFormat = SimpleDateFormat("yyyyMMddhhmm")
