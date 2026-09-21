package com.panopset.desk.utilities.dash.publish

import com.panopset.compat.Fileop
import com.panopset.compat.opSecureCopyPut
import com.panopset.desk.utilities.dash.GrandCentralStation

class NodeDeploy(val gcs: GrandCentralStation) {
    val rhd = gcs.createRemoteHostData()
    val d = rhd.d
    val u = rhd.u

    fun publish() {
        val projectName = gcs.projectDir.name
        val projectBaseDir = gcs.projectDirectorySelector.createDir()
        val serviceSpecPath = Fileop.combinePaths(
            projectBaseDir, "$projectName-node.service")
        val toScp = "/home/$u"
        opSecureCopyPut(rhd, serviceSpecPath, toScp)
    }
}
