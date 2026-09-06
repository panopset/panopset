package com.panopset.desk.utilities.dash.rows

import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.desk.utilities.dash.sm.ServerState

class ServerLetsEncryptDone(gcs: GrandCentralStation): Report(gcs) {
    override fun updateOutput() {
        val rhd = gcs.createRemoteHostData()
        val d = rhd.d
        val slabRawDirectory = gcs.createSlabRawDirectory()
        val rawEnabled = slabRawDirectory.exists() && slabRawDirectory.isDirectory()
        if (gcs.currentServerState == ServerState.LetsEncryptDone) {
            gcs.publishRawBtn.isDisable = !rawEnabled
            gcs.publishDownloadsBtn.isDisable = false
            gcs.publishBeamBtn.isDisable = false
            gcs.publishSiteBtn.isDisable = false
        }
        if (rawEnabled) {
            showStateReport("Ready to publish to $d!")
        } else {
            showStateReport("Ready to publish to $d!\n\n" +
                    "Raw disabled because there is no ${slabRawDirectory.absolutePath}.")
        }
    }
}
