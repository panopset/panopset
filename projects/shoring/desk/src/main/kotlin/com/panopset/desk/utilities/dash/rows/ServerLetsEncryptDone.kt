package com.panopset.desk.utilities.dash.rows

import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.desk.utilities.dash.sm.ServerState

class ServerLetsEncryptDone(gcs: GrandCentralStation): Report(gcs) {
    override fun updateOutput() {
        val rhd = gcs.createRemoteHostData()
        val d = rhd.d
        val slabRawDirectory = gcs.createSlabRawDirectory()
        val slabTemplateDriverFile = gcs.createSlabTemplateDriverFile()
        val projectsBeamDirectory = gcs.createProjectsBeamDirectory()
        val projectsShoringDirectory = gcs.createProjectsShoringDirectory()
        val isRawEnabled = slabRawDirectory.exists() && slabRawDirectory.isDirectory()
        val isBeamEnabled = projectsBeamDirectory.exists() && projectsBeamDirectory.isDirectory()
        val isDownloadEnabled = projectsShoringDirectory.exists() && projectsShoringDirectory.isDirectory()
        val isHtmlEnabled = slabTemplateDriverFile.exists() && slabTemplateDriverFile.isFile()
        if (gcs.currentServerState == ServerState.LetsEncryptDone) {
            gcs.publishRawBtn.isDisable = !isRawEnabled
            gcs.publishDownloadsBtn.isDisable = !isDownloadEnabled
            gcs.publishBeamBtn.isDisable = !isBeamEnabled
            gcs.publishSiteBtn.isDisable = false
        }
        if (isRawEnabled) {
            showStateReport("Ready to publish to $d!")
        } else {
            showStateReport("Ready to publish to $d!\n\n" +
                    "Raw disabled because there is no ${slabRawDirectory.absolutePath}.")
        }
    }
}
