package com.panopset.desk.utilities.dash.rows

import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.desk.utilities.dash.sm.ServerState
import java.io.StringWriter

class ServerLetsEncryptDone(gcs: GrandCentralStation): Report(gcs) {
    override fun updateOutput() {
        val slabRawDirectory = gcs.createSlabRawDirectory()
        val slabTemplateDriverFile = gcs.createSlabTemplateDriverFile()
        val projectsBeamDirectory = gcs.createProjectsBeamDirectory()
        val projectsShoringDirectory = gcs.createProjectsShoringDirectory()
        val projectsPublicDirectory = gcs.createPublicDirectory()
        val isRawEnabled = slabRawDirectory.exists() && slabRawDirectory.isDirectory()
        val isBeamEnabled = projectsBeamDirectory.exists() && projectsBeamDirectory.isDirectory()
        val isDownloadEnabled = projectsShoringDirectory.exists() && projectsShoringDirectory.isDirectory()
        val isSiteEnabled = slabTemplateDriverFile.exists() && slabTemplateDriverFile.isFile()
        val isNodeEnabled = projectsPublicDirectory.exists() && projectsPublicDirectory.isDirectory()
        if (gcs.currentServerState == ServerState.LetsEncryptDone) {
            gcs.publishRawBtn.isDisable = !isRawEnabled
            gcs.publishDownloadsBtn.isDisable = !isDownloadEnabled
            gcs.publishBeamBtn.isDisable = !isBeamEnabled
            gcs.publishSiteBtn.isDisable = !isSiteEnabled
            gcs.publishNodeBtn.isDisable = !isNodeEnabled
        }
        val sw = StringWriter()
        if (isRawEnabled) {
            sw.append("Ready to publish raw directory ${slabRawDirectory.absolutePath}!\n")
        } else {
            sw.append("Raw disabled because there is no ${slabRawDirectory.absolutePath}.\n")
        }
        if (isDownloadEnabled) {
            sw.append("Ready to publish Downloads!\n")
        } else {
            sw.append("Downloads disabled because there is no ${projectsShoringDirectory.absolutePath}.\n")
        }
        if (isBeamEnabled) {
            sw.append("Ready to publish Springboot Beam!\n")
        } else {
            sw.append("Beam disabled because there is no ${projectsBeamDirectory.absolutePath} Springboot project.\n")
        }
        if (isSiteEnabled) {
            sw.append("Ready to publish website!\n")
            sw.append("Node disabled because this is a nginx served website.\n")
        } else {
            sw.append("Site disabled because there is no Flywheel template driver file for the website:\n" +
                    "     ${slabTemplateDriverFile.absolutePath}.\n")
            if (isNodeEnabled) {
                sw.append("Ready to publish Node website!\n")
            } else {
                sw.append("Node disabled because there is no public folder:\n" +
                        "     ${projectsPublicDirectory.absolutePath}.\n")
            }
        }
        showStateReport(sw.toString())
    }
}
