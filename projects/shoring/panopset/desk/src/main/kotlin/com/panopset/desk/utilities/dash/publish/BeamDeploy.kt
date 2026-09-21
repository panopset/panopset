package com.panopset.desk.utilities.dash.publish

import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import com.panopset.compat.opSecureCopyPut
import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.flywheel.FlywheelBuilder
import javafx.application.Platform
import java.io.File

class BeamDeploy(val gcs: GrandCentralStation) {
    val rhd = gcs.createRemoteHostData()
    val d = rhd.d
    val h = rhd.h

    fun publish() {
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
}
