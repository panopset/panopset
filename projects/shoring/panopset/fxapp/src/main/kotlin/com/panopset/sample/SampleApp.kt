package com.panopset.sample

import com.panopset.fxapp.ApplicationBranding
import com.panopset.fxapp.ApplicationInfo
import com.panopset.fxapp.BrandedApp
import com.panopset.fxapp.FxDoc
import com.panopset.fxapp.PanComponentFactory.createPanTabPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.control.Tab

class SampleApp: BrandedApp(object: ApplicationInfo {
    override fun getApplicationBranding(): ApplicationBranding {
        return SampleBranding()
    }

    override fun getApplicationDisplayName(): String {
        return "SampleApp"
    }

    override fun getDescription(): String {
        return "Sample application."
    }

    override fun updateVersionMessage(fxDoc: FxDoc) {
        fxDoc.dspmsg("Green log entry.")
        fxDoc.debug("Yellow log entry.")
        fxDoc.warn("Orange log entry.")
        fxDoc.errorMsg("Red log entry.")
    }
}) {

    override fun createDynapane(fxDoc: FxDoc): Pane {
        val bp: BorderPane = createStandardMenubarBorderPane(fxDoc)
        val tp = createPanTabPane(fxDoc, "sampleBPcenter")
        tp.tabs.add(Tab("SomeTab"))
        bp.center = tp
        return bp
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SampleApp().go()
        }
    }
}
