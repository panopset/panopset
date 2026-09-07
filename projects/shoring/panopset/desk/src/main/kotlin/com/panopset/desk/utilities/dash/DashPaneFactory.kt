package com.panopset.desk.utilities.dash

import com.panopset.desk.utilities.dash.rows.DomainInfoRow
import com.panopset.desk.utilities.dash.rows.ServerInfoRow
import com.panopset.fxapp.FxDoc
import com.panopset.fxapp.PanComponentFactory
import javafx.scene.layout.VBox

class DashPaneFactory(fxDoc: FxDoc, osInfoMap: Map<String, String>) {
    val gcs = GrandCentralStation(fxDoc, osInfoMap)
    val domainInfoRow = DomainInfoRow(gcs)
    val serverInfoRow = ServerInfoRow(gcs)
    val outputTA = gcs.outputTA

    fun createCenterPane(): VBox {
        val firstRow = domainInfoRow.createRow()
        val secondRow = serverInfoRow.createRow()
        val rtn = PanComponentFactory.createPanVBox(firstRow, secondRow, outputTA)
        return rtn
    }

    fun updateOutput() {
        gcs.updateOutput()
    }
}
