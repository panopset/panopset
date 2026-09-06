package com.panopset.desk.utilities

import com.panopset.PanopsetBranding
import com.panopset.compat.Logz
import com.panopset.fxapp.*
import com.panopset.desk.utilities.lowerclass.VersionParser
import com.panopset.fxapp.PanComponentFactory.createPanButton
import com.panopset.fxapp.PanComponentFactory.createPanCheckBox
import com.panopset.fxapp.PanComponentFactory.createPanHBox
import com.panopset.fxapp.PanComponentFactory.createPanScrollPane
import com.panopset.fxapp.PanComponentFactory.createPanTextArea
import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import java.io.File
import java.io.IOException

class LowerClass: BrandedApp(
    object: ApplicationInfo {
        override fun getApplicationBranding(): ApplicationBranding {
            return PanopsetBranding()
        }

        override fun getApplicationDisplayName(): String {
            return "Lower Class"
        }

        override fun getDescription(): String {
            return "Generate a report on minimum JDKs for the class files found in a jar, or a repository directory."
        }
    }
) {
    private lateinit var lcReport: Button
    private lateinit var lcOut: TextArea
    private lateinit var lcFileOrDirSelect: PanFileOrDirSelectorPanel
    private lateinit var lcDetails: CheckBox

    override fun createDynapane(fxDoc: FxDoc): Pane {
        lcReport = createPanButton(fxDoc, { refresh() }, "Report", false, "Generate report.")
        lcOut = createPanTextArea(fxDoc, "Report on selected file or directory to appear here.",
            "Please click the Report button, after selecting jar file or class directory.")
        lcFileOrDirSelect = PanFileOrDirSelectorPanel(fxDoc, "lcFileOrDirSelect")
        lcDetails = createPanCheckBox(fxDoc, "lcDetails", "details")

        val b: BorderPane = createStandardMenubarBorderPane(fxDoc)
        b.center = createCenter()
        return b
    }
    private fun createCenter(): BorderPane {
        val bp = BorderPane()
        bp.top = createPanHBox(lcFileOrDirSelect.pane, lcDetails, lcReport)
        bp.center = createPanScrollPane(lcOut)
        return bp
    }

    private fun refresh() {
        lcOut.text = ""
        if (lcReport.isDisabled) {
            Logz.warn("Please wait for running process to complete.")
            return
        }
        lcReport.isDisable = true
        Platform.runLater {
            val file = lcFileOrDirSelect.createFile()
            val vp0 = createVersionParser(file)
            try {
                if (lcDetails.isSelected) {
                    if (file.isDirectory) {
                        Logz.warn("Details not available on a directory report.")
                        lcOut.appendText(vp0.summaryReport)
                    } else {
                        lcOut.appendText(vp0.summaryReport)
                    }
                } else {
                    lcOut.appendText(vp0.summaryReport)
                }
            } catch (ex: IOException) {
                Logz.errorEx(ex)
            }
            lcReport.isDisable = false
        }
    }

    private fun createVersionParser(file: File): VersionParser {
        return if (file.exists()
            && file.canRead()
        ) {
            VersionParser(file)
        } else {
            VersionParser()
        }

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            LowerClass().go()
        }
    }
}
