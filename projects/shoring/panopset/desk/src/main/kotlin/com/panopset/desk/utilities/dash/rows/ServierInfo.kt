package com.panopset.desk.utilities.dash.rows

import com.panopset.desk.utilities.dash.GrandCentralStation

class ServerInfo(gcs: GrandCentralStation) {
    val rhd = gcs.createRemoteHostData()
    val h = rhd.h
    val u = rhd.u
    val d = rhd.d
    val projectName = gcs.launchDir.name

    fun lineSep(): String {
        return "\n***********************************************************************************************\n"
    }

    fun installServiceInstructions(): String {
        return "    cd\n" +
                "    sudo mkdir /opt/$projectName-node\n" +
                "    sudo chown $u:$u /opt/$projectName-node\n" +
                "    sudo mv $projectName-node.service /etc/systemd/system/\n" +
                "    sudo systemctl daemon-reload\n" +
                "    sudo systemctl enable --now $projectName-node\n" +
                "    sudo systemctl status $projectName-node\n" +
                "    sudo reboot 0\n"
    }

    fun deploymentSetupInstructions(): String {
        return  "We'll need a place to deploy the application:\n" +
                "    ssh $h\n" +
                "If this is the first deployment, you need to create the deployment directory:\n" +
                "    mkdir -p /opt/$projectName-node\n" +
                "If this is a re-deploy, you just need to clear the deployment directory:\n" +
                "    rm -rf /opt/$projectName-node/*\n"
    }
}
