package com.panopset.desk.utilities.dash.publish

import com.panopset.desk.utilities.dash.GrandCentralStation

class Deployment(val gcs: GrandCentralStation) {
    val rhd = gcs.createRemoteHostData()
    val d = rhd.d

    fun publishBeam(){BeamDeploy(gcs).publish()}
    fun publishRaw() {RawDeploy(gcs).publish()}
    fun publishDownloads() {DownloadsDeploy(gcs).publish()}
    fun publishSite() {SiteDeploy(gcs).publish()}
    fun publishNode() {NodeDeploy(gcs).publish()}
}
