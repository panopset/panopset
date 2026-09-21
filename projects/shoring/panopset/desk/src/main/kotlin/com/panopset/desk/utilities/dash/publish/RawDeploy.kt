package com.panopset.desk.utilities.dash.publish

import com.panopset.compat.opSecureCopyPut
import com.panopset.desk.utilities.dash.GrandCentralStation
import javafx.application.Platform

class RawDeploy(val gcs: GrandCentralStation) {
    val rhd = gcs.createRemoteHostData()
    val d = rhd.d
    val remoteBase = "/var/www/$d/html"

    fun publish() {
        val toScp = remoteBase
        val fromScpFile = gcs.createSlabRawDirectory()
        val fromScp = fromScpFile.absolutePath
        if (fromScpFile.exists()) {
            Platform.runLater {
                opSecureCopyPut(rhd, fromScp, toScp)
            }
        }
    }
}
