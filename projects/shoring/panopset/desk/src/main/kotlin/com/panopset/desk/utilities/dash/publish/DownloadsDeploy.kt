package com.panopset.desk.utilities.dash.publish

import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import com.panopset.compat.opSecureCopyPut
import com.panopset.desk.utilities.dash.GrandCentralStation
import javafx.application.Platform
import java.io.File

class DownloadsDeploy(val gcs: GrandCentralStation) {
    val rhd = gcs.createRemoteHostData()
    val d = rhd.d
    val remoteBase = "/var/www/$d/html"

    fun publish() {
        if (gcs.platformKey.isEmpty()) {
            Logz.errorMsg("No platform key provided as Dash launch parameter.")
            return
        }
        publishDownloadsFor(gcs.platformKey)
    }

    private fun publishDownloadsFor(osPath: String) {
        val projectName = gcs.projectDir.name
        val projectBaseDir = gcs.projectDirectorySelector.createDir()
        val fromScpFile = File(Fileop.combinePaths(
            projectBaseDir,
            "target"
        ))
        val fromScp = fromScpFile.absolutePath
        val toScp = "$remoteBase/downloads/$osPath"
        val skipDirectory = if (osPath == "mac") {
            "$projectName.app"
        } else {
            projectName
        }
        if (fromScpFile.exists()) {
            Platform.runLater {
                opSecureCopyPut(rhd, fromScp, toScp,
                    arrayListOf(skipDirectory))
            }
        } else {
            Logz.errorMsg("Could not find ${fromScpFile.absolutePath}")
        }
    }
}
