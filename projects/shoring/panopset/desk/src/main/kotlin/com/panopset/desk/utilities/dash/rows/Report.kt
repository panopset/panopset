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

    fun createFirstHtmlFileInstructions(): String {
        val rhd = gcs.createRemoteHostData()
        val d = rhd.d
       return "Make sure you have something to display:\n\n" +
        "    vim /var/www/$d/html/index.html\n\n" +
        "and put something like this in there: \n\n" +
        "<html><head><title>$d</title></head><body><h1>Under construction</h1></body></html>\n\n" +
        REFRESH_PROMPT
    }
}

const val REFRESH_PROMPT = "and hit the refresh button above left.\n\n"
