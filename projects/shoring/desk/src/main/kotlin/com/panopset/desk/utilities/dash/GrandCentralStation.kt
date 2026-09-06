package com.panopset.desk.utilities.dash

import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import com.panopset.compat.RemoteHostData
import com.panopset.desk.utilities.dash.publish.Deployment
import com.panopset.desk.utilities.dash.rows.Report
import com.panopset.desk.utilities.dash.rows.ServerBrandNew
import com.panopset.desk.utilities.dash.rows.ServerDomainEstablished
import com.panopset.desk.utilities.dash.rows.ServerInvalid
import com.panopset.desk.utilities.dash.rows.ServerLetsEncryptDone
import com.panopset.desk.utilities.dash.rows.ServerPublished
import com.panopset.desk.utilities.dash.rows.ServerUnknown
import com.panopset.desk.utilities.dash.rows.ServerUserConfigured
import com.panopset.desk.utilities.dash.sm.Server
import com.panopset.desk.utilities.dash.sm.ServerState
import com.panopset.fxapp.FxDoc
import com.panopset.fxapp.PanComponentFactory
import com.panopset.fxapp.PanDirSelectorPanel
import java.io.File

class GrandCentralStation(val fxDoc: FxDoc, val osInfoMap: Map<String, String>) {
    var currentServerState = ServerState.Initial
    var projectDir: File = File(System.getProperty("user.dir"))
    var server = Server()
    val projectDirectorySelector = PanDirSelectorPanel(fxDoc, "projectDirectory", "Project")
    val outputTA = PanComponentFactory.createPanTextArea(fxDoc)
    val ipTF = PanComponentFactory.createPanInputTextFieldHGrow(fxDoc, "ip",
        "IP address", "IP address of server, defined in ~/.ssh/config IdentityFile, ie \"127.0.0.1\" .")
    val domainTF = PanComponentFactory.createPanInputTextFieldHGrow(fxDoc, "website",
        "website", "Target website domain name, ie \"panopset.com\" .")
    val platformKey = osInfoMap["PLATFORM_KEY"]?:""
    val platformKeyLabel = PanComponentFactory.createPanLabel(fxDoc, "Platform Key: $platformKey")
    val refreshBtn = PanComponentFactory.createPanButton(fxDoc,
        {
            updateOutput()
        }, "Refresh", false, "Refresh host definition in ~/.ssh/config.")
    val publishRawBtn = PanComponentFactory.createPanButton(fxDoc,
        {
            val deployment = Deployment(this)
            deployment.publishRaw()
        }, "Raw", false, "Publish raw files to website.")
    val publishDownloadsBtn = PanComponentFactory.createPanButton(fxDoc,
        {
            val deployment = Deployment(this)
            deployment.publishDownloads()
        }, "Downloads", false, "Publish downloads to website.")
    val publishBeamBtn = PanComponentFactory.createPanButton(fxDoc,
        {
            val deployment = Deployment(this)
            deployment.publishBeam()
        }, "Beam", false, "Publish beam to website.")
    val publishSiteBtn = PanComponentFactory.createPanButton(fxDoc,
        {
            val deployment = Deployment(this)
            deployment.publishSite()
        }, "Site", false, "Publish website.")
    fun disablePublishButtons() {
        publishRawBtn.isDisable = true
        publishDownloadsBtn.isDisable = true
        publishBeamBtn.isDisable = true
        publishSiteBtn.isDisable = true
    }
    fun updateOutput() {
        disablePublishButtons()
        currentServerState = ServerState.Initial

        // TODO: There has to be a more general, better way to do this, without affecting apps that want to save
        //       error messages between launches.
        Logz.clear()
        fxDoc.fxDocMessage.setMsg("")

        server.state = ServerState.Unknown
        var report: Report
        while (currentServerState != server.state) {
            currentServerState = server.state
            report = when (server.state) {
                ServerState.Unknown -> ServerUnknown(this)
                ServerState.Invalid -> ServerInvalid(this)
                ServerState.BrandNew -> ServerBrandNew(this)
                ServerState.UserConfigured -> ServerUserConfigured(this)
                ServerState.DomainEstablished -> ServerDomainEstablished(this)
                ServerState.LetsEncryptDone -> ServerLetsEncryptDone(this)
                ServerState.Published -> ServerPublished(this)
                ServerState.Initial -> throw RuntimeException("Impossible, reserved for initial currentServerState.")
            }
            report.updateOutput()
        }
    }

    fun createRemoteHostData(): RemoteHostData {
        return createRemoteHostData(ipTF.text, domainTF.text)
    }

    private fun createRemoteHostData(ip: String, domain: String): RemoteHostData {
        val configValues = SshConfig.getConfigForIP(ip)
        if (configValues.isEmpty()) return RemoteHostData(ip, "", domain, "", "")
        return RemoteHostData(
            ip,
            configValues[CONFIG_HOST_INDEX],
            domain,
            configValues[CONFIG_USER_INDEX],
            configValues[CONFIG_IDENTITY_FILE_INDEX],)
    }

    fun createSlabRawDirectory(): File {
        return File(Fileop.combinePaths(projectDirectorySelector.createDir(),
            "projects/slab/pan/raw"))
    }
}
