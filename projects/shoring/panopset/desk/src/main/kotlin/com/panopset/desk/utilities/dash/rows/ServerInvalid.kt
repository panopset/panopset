package com.panopset.desk.utilities.dash.rows

import com.panopset.desk.utilities.dash.GrandCentralStation

class ServerInvalid(gcs: GrandCentralStation): Report(gcs) {
    override fun updateOutput() {
        showStateReport("")
    }
}
