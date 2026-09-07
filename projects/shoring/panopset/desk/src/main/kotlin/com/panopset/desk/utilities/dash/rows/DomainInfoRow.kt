package com.panopset.desk.utilities.dash.rows

import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.fxapp.PanComponentFactory
import javafx.scene.layout.HBox

class DomainInfoRow(gcs: GrandCentralStation) {
    val ipTF = gcs.ipTF
    val domainTF = gcs.domainTF
    val platformKeyLabel = gcs.platformKeyLabel
    val projectDirectorySelector = gcs.projectDirectorySelector

    fun createRow(): HBox {
        return PanComponentFactory.createPanHBox(platformKeyLabel,
            projectDirectorySelector.pane, domainTF, ipTF, )
    }
}
