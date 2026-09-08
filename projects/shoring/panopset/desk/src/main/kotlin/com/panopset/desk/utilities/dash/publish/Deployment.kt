package com.panopset.desk.utilities.dash.publish

import com.panopset.compat.AppVersion
import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import com.panopset.compat.opSecureCopyGet
import com.panopset.compat.opSecureCopyPut
import com.panopset.compat.props2map
import com.panopset.desk.utilities.GenerateDownloadsTable
import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.flywheel.FlywheelBuilder
import java.io.File
import javafx.application.Platform
import java.text.SimpleDateFormat
import java.util.Date

class Deployment(val gcs: GrandCentralStation) {
    val rhd = gcs.createRemoteHostData()
    val d = rhd.d
    val h = rhd.h
    val remoteBase = "/var/www/$d/html"

    fun publishRaw() {
        val toScp = remoteBase
        val fromScpFile = gcs.createSlabRawDirectory()
        val fromScp = fromScpFile.absolutePath
        if (fromScpFile.exists()) {
            Platform.runLater {
                opSecureCopyPut(rhd, fromScp, toScp)
            }
        }
    }

    fun publishDownloads() {
        if (gcs.platformKey.isEmpty()) {
            Logz.errorMsg("No platform key provided as Dash launch parameter.")
            return
        }
        publishDownloadsFor(gcs.platformKey)
    }

    fun publishBeam() {
        // TODO: assemble all environment variable checks in a single validation method.
        val projectBaseDir = gcs.projectDirectorySelector.createDir()
        val userName = System.getenv()["PAN_SV_NM"]
        if (userName.isNullOrEmpty()) {
            Logz.errorMsg("PAN_SV_NM not defined as an environment variable. " +
                    "See docs/setup/env.md")
            return
        }
        val beamServiceTemplateFile = File(Fileop.combinePaths(
            projectBaseDir,
            "/projects/slab/pan/templates/beam/beamService.txt"
        ))
        val tmpBeamDirectory = File(Fileop.combinePaths(
            projectBaseDir,
            "/tmp/beam"
        ))
        val beamJarFile = File(Fileop.combinePaths(
            projectBaseDir,
            "/projects/beam/target/beam.jar"
        ))
        FlywheelBuilder().file(beamServiceTemplateFile)
            .targetDirectory(tmpBeamDirectory).construct().exec()
        val fromScp = tmpBeamDirectory.absolutePath
        val fromScpBeamJar = beamJarFile.absolutePath
        val toScp = "/home/$userName/"
        val fromScpFile = File(fromScp)
        if (fromScpFile.exists()) {
            Platform.runLater {
                opSecureCopyPut(rhd, fromScp, toScp)
                opSecureCopyPut(rhd, fromScpBeamJar, toScp)
                gcs.outputTA.text = "Next... \n\n" +
                        "    ssh $h\n" +
                        "    chmod +x installservice.sh\n" +
                        "    ./installservice.sh\n" +
                        "    sudo reboot 0\n" +
                        "Take a short break, then...\n" +
                        "    ssh $h\n" +
                        "    sudo netstat -tulpn\n\n" +
                        "Verify there is a java process listening on port 8080.\n\n" +
                        "Next, add in /etc/nginx/sites-available/$d, before the location / entry:\n\n" +
                        " location /beam/ {\n" +
                        "  proxy_pass http://localhost:8080/;\n" +
                        " }\n\n" +
                        "    sudo nginx -t\n" +
                        "    sudo reboot 0" +
                        "\n"
            }
        } else {
            Logz.errorMsg("Could not find ${fromScpFile.absolutePath}")
        }
    }

    fun publishSite() {
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
                    "<a href=\"https://$d\">$d</a>."
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

private val dashDateFormat = SimpleDateFormat("yyyy-MM-dd")
private val timestampFormat = SimpleDateFormat("yyyyMMddhhmm")
