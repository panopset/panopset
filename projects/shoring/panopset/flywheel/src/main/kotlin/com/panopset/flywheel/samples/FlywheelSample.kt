package com.panopset.flywheel.samples

import java.io.Serializable

class FlywheelSample : Serializable {
    var name = ""
    var desc = ""
    var listText = ""
    var templateText = ""
    var tokens = ""
    var splitz = ""
    var lineBreaks = true
    var listBreaks = true

    override fun toString(): String {
        return String.format("%s:%s", name, templateText)
    }
}
