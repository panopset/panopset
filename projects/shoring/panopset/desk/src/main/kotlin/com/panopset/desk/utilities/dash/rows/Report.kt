package com.panopset.desk.utilities.dash.rows

import com.panopset.desk.utilities.dash.GrandCentralStation
import javafx.application.Platform

abstract class Report(val gcs: GrandCentralStation) {
    protected fun showStateReport(message: String) {
        Platform.runLater {
            gcs.outputTA.text = "${gcs.server} \n$message"
        }
    }

    abstract fun updateOutput()
}

const val REFRESH_PROMPT = "and hit the refresh button above left.\n\n"
