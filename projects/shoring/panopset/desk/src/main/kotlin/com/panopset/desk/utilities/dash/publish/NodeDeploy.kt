package com.panopset.desk.utilities.dash.publish

import com.panopset.compat.Fileop
import com.panopset.compat.Stringop
import com.panopset.compat.opSecureCopyPut
import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.flywheel.FlywheelBuilder
import java.io.File

class NodeDeploy(val gcs: GrandCentralStation) {
    val rhd = gcs.createRemoteHostData()
    val d = rhd.d
    val u = rhd.u
    val projectName = gcs.projectDir.name
    val projectBaseDir = gcs.projectDirectorySelector.createDir()

    fun publish() {
        generateServiceFile()
        val fromScpServiceSpec = Fileop.combinePaths(
            projectBaseDir, "tmp/$projectName-node.service")
        val toScpUserHome = "/home/$u"
        opSecureCopyPut(rhd, fromScpServiceSpec, toScpUserHome)
        scp("package.json")
        scp("package-lock.json")
        scpDir("src")
        scpDir("public")
    }

    private fun scp(artifact: String) {
        val fromPath = Fileop.combinePaths(
            projectBaseDir, artifact
        )
        opSecureCopyPut(rhd, fromPath, "/opt/$projectName-node/")
    }

    private fun scpDir(artifact: String) {
        val fromPath = Fileop.combinePaths(
            projectBaseDir, artifact
        )
        opSecureCopyPut(rhd, fromPath, "/opt/$projectName-node/$artifact")
    }

    private fun generateServiceFile() {
        val result = FlywheelBuilder()
            .inputString(serviceFileTemplate)
            .map("n", projectName)
            .map("nc", Stringop.capitalize(projectName))
            .map("u", u)
            .construct().exec()
        if (Stringop.isPopulated(result)) {
            Fileop.write(result, File("$projectBaseDir/tmp/${projectName}-node.service"))
        }
    }
}

private val serviceFileTemplate =
    "[Unit]\n" +
            "Description=\${nc} Node.js App\n" +
            "After=network-online.target\n" +
            "Wants=network-online.target\n" +
            "\n" +
            "[Service]\n" +
            "Type=simple\n" +
            "User=\${u}\n" +
            "Group=\${u}\n" +
            "WorkingDirectory=/opt/\${n}-node\n" +
            "ExecStart=/usr/bin/node /opt/\${n}-node/src/server.js\n" +
            "EnvironmentFile=-/etc/redleaf-node/\${n}-node.env\n" +
            "Environment=NODE_ENV=production\n" +
            "Environment=HOST=127.0.0.1\n" +
            "Environment=PORT=3000\n" +
            "Restart=on-failure\n" +
            "RestartSec=5\n" +
            "NoNewPrivileges=true\n" +
            "PrivateTmp=true\n" +
            "ProtectSystem=full\n" +
            "ProtectHome=true\n" +
            "ProtectControlGroups=true\n" +
            "ProtectKernelModules=true\n" +
            "ProtectKernelTunables=true\n" +
            "RestrictSUIDSGID=true\n" +
            "LockPersonality=true\n" +
            "CapabilityBoundingSet=\n" +
            "AmbientCapabilities=\n" +
            "RestrictAddressFamilies=AF_UNIX AF_INET AF_INET6\n" +
            "\n" +
            "[Install]\n" +
            "WantedBy=multi-user.target\n"