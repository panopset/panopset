package com.panopset.desk.utilities.dash.publish

import com.panopset.compat.Fileop
import com.panopset.compat.opSecureCopyPut
import com.panopset.desk.utilities.dash.GrandCentralStation

class NodeDeployStatic(val gcs: GrandCentralStation) {
    val rhd = gcs.createRemoteHostData()
    val d = rhd.d
    val projectBaseDir = gcs.projectDirectorySelector.createDir()

    fun publish() {
        val fromPath = Fileop.combinePaths(
            projectBaseDir, "dist"
        )
        opSecureCopyPut(rhd, fromPath, "/var/www/$d/html/")
    }

}
