package com.panopset.desk.utilities.dash.rows

import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.fxapp.PanComponentFactory
import javafx.scene.layout.HBox

class ServerInfoRow(val gcs: GrandCentralStation) {
    val fxDoc = gcs.fxDoc
    val refreshBtn = gcs.refreshBtn
    val publishRawBtn = gcs.publishRawBtn
    val publishDownloadsBtn = gcs.publishDownloadsBtn
    val publishBeamBtn = gcs.publishBeamBtn
    val publishSiteBtn = gcs.publishSiteBtn

    fun createRow(): HBox {
        gcs.disablePublishButtons()
        return PanComponentFactory.createPanHBox(
            refreshBtn, publishRawBtn, publishDownloadsBtn, publishBeamBtn, publishSiteBtn
        )
    }
}
