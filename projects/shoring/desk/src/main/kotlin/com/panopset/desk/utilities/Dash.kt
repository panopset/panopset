package com.panopset.desk.utilities

import com.panopset.PanopsetBranding
import com.panopset.desk.utilities.dash.DashOperatingSystemProperties
import com.panopset.desk.utilities.dash.DashPaneFactory
import com.panopset.fxapp.ApplicationBranding
import com.panopset.fxapp.ApplicationInfo
import com.panopset.fxapp.BrandedApp
import com.panopset.fxapp.FxDoc
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import kotlin.system.exitProcess

class Dash(env: String) : BrandedApp(
    object: ApplicationInfo {
        override fun getApplicationBranding(): ApplicationBranding {
            return PanopsetBranding()
        }

        override fun getApplicationDisplayName(): String {
            return "Dash"
        }

        override fun getDescription(): String {
            return "Website deployment dashboard."
        }
    }
) {
    private val map = HashMap<FxDoc, DashPaneFactory>()
    private val dashOperatingSystemProperties = DashOperatingSystemProperties(env)
    val osInfoMap = dashOperatingSystemProperties.load()

    override fun createDynapane(fxDoc: FxDoc): Pane {
        val b: BorderPane = createStandardMenubarBorderPane(fxDoc)
        val dashPaneFactory = DashPaneFactory(fxDoc, osInfoMap)
        b.center = dashPaneFactory.createCenterPane()
        map[fxDoc] = dashPaneFactory
        return b
    }

    override fun afterShow(fxDoc: FxDoc) {
        // Uncomment these lines if you want it to refresh on launch.
//        val dpf: DashPaneFactory = map[fxDoc] ?: return
//        dpf.updateOutput()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.isEmpty()) {
                println("Environment parameter expected.")
                exitProcess(1)
            }
            Dash(args[0]).go()
        }
    }
}
