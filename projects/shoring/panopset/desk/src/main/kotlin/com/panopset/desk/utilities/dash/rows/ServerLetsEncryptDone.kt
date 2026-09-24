package com.panopset.desk.utilities.dash.rows

import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.desk.utilities.dash.sm.ServerState
import java.io.StringWriter

class ServerLetsEncryptDone(gcs: GrandCentralStation): Report(gcs) {
    val rhd = gcs.createRemoteHostData()
    val h = rhd.h
    val u = rhd.u
    val d = rhd.d
    val projectName = gcs.projectDir.name

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
                sw.append("\nThe server is ready to publish your Javascript Node project, " +
                        "but there are some things we need to do before pressing the Node button:\n\n" +
                        "We'll need a place to deploy the application:\n" +
                        "    ssh $h\n" +
                        "If this is a re-deploy, you just need to clear the deployment directory:\n" +
                        "    rm -rf /opt/$projectName-node/*\n" +
                        "If this is the first deployment, you need to create the deployment directory,\n" +
                        "Once all that is done, ready to publish Node website,\n" +
                        "by clicking the Node button above right.\n" +
                        "Then: \n" +
                        "    cd /opt/$projectName-node\n" +
                        "    npm i\n" +
                        "    npm start\n" +
                        "    sudo netstat -tulpn\n" +
                        "Verify you see it running on port 3000, then set up your proxy pass:\n" +
                        "    sudo vim /etc/nginx/sites-available/$d\n" +
                        "and replace the location / section with\n\n" +
                        "      location / {\n" +
                        "            proxy_pass http://localhost:3000;\n" +
                        "      }\n\n" +
                        "then \n" +
                        "    sudo nginx -t\n" +
                        "    sudo reboot 0\n" +
                        "You don't want to have to manually start it each time, so install the service:\n" +
                        "    cd\n" +
                        "    sudo mkdir /opt/$projectName-node\n" +
                        "    sudo chown $u:$u /opt/$projectName-node\n" +
                        "    sudo mv $projectName-node.service /etc/systemd/system/\n" +
                        "    sudo systemctl daemon-reload\n" +
                        "    sudo systemctl enable --now $projectName-node\n" +
                        "    sudo systemctl status $projectName-node\n" +
                        "    sudo reboot 0\n" +
                        "\n" +
                        "")
            } else {
                sw.append("Node disabled because there is no public folder:\n" +
                        "     ${projectsPublicDirectory.absolutePath}.\n")
            }
        }
        showStateReport(sw.toString())
    }
}
