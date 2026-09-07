package com.panopset.desk.utilities.dash.rows

import com.panopset.desk.utilities.dash.GrandCentralStation

class ServerPublished(gcs: GrandCentralStation): Report(gcs) {
    override fun updateOutput() {
        showStateReport("published")
    }
}
