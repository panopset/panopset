package com.panopset.desk.security

import com.panopset.PanopsetBranding
import com.panopset.compat.Logz
import com.panopset.compat.TextProcessor
import com.panopset.fxapp.*
import com.panopset.fxapp.PanComponentFactory.createPanButton
import com.panopset.fxapp.PanComponentFactory.createPanCheckBox
import com.panopset.fxapp.PanComponentFactory.createPanHBox
import com.panopset.fxapp.PanComponentFactory.createPanScrollPane
import com.panopset.fxapp.PanComponentFactory.createPanTextArea
import com.panopset.marin.secure.checksums.ChecksumReport
import com.panopset.marin.secure.checksums.ChecksumType
import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.TextArea
import javafx.scene.control.Tooltip
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox

class Checksum: BrandedApp (
    object: ApplicationInfo {
        override fun getApplicationBranding(): ApplicationBranding {
            return PanopsetBranding()
        }

        override fun getApplicationDisplayName(): String {
            return "Checksum"
        }

        override fun getDescription(): String {
            return "Validate files with various checksums you might typically encounter."
        }
    }
) {

    override fun createDynapane(fxDoc: FxDoc): Pane {
        val csCheckBoxesHBox = HBox()
        var isAllOn = true
        val csOut = createPanTextArea(fxDoc)
        val csCheckBoxes = ArrayList<CheckBox>()
        for (cst in ChecksumType.entries) {
            val cb = createPanCheckBox(fxDoc, "id_cb${cst.name}", cst.name)
            cb.id = "cs_algbtn_" + cst.name
            if ("BYTES" == cst.name) {
                cb.tooltip = Tooltip(
                    "This is just the byte count for the file, not any kind of checksum."
                )
            }
            csCheckBoxesHBox.children.add(cb)
            csCheckBoxes.add(cb)
        }
        val csFileSelect = PanFileOrDirSelectorPanel(fxDoc, "csFileOrDirSelect")
        val csChecksum: Button = createPanButton(fxDoc,
            {
                doProcess(csOut, csFileSelect, csCheckBoxes)
            }, "_Checksum", true,
            "Run checkbox specified checksums on selected file."
        )

        val csAll = createPanButton(fxDoc, {
            for (cb in csCheckBoxes) {
                cb.isSelected = isAllOn
            }
            isAllOn = !isAllOn
        }, "_All", true, "Toggle select/deselect all checkboxes.")
        val b: BorderPane = createStandardMenubarBorderPane(fxDoc)
        val bp = BorderPane()
        val topControls = VBox()
        topControls.children.addAll(
            createPanHBox(csChecksum, csAll, csFileSelect.pane),
            createCsCheckBoxesHBox(csCheckBoxes)
        )
        bp.top = topControls
        bp.center = createPanScrollPane(csOut)
        b.center = bp
        return b
    }

    private fun createCsCheckBoxesHBox(csCheckBoxes: ArrayList<CheckBox>): HBox {
        val rtn = HBox()
        for (cb in csCheckBoxes) {
            rtn.children.add(cb)
        }
        return rtn
    }

    private fun doProcess(csOut: TextArea, csFileSelect: PanFileOrDirSelectorPanel, csCheckBoxes: ArrayList<CheckBox>) {
        val types = getSelectedTypes(csCheckBoxes)
        if (types.isEmpty()) {
            Logz.warn("Nothing selected.")
            csOut.text = "Nothing selected."
            return
        }
        Logz.clear()
        csOut.text = ""
        createReport(types, csFileSelect, csOut)
    }

    private fun createReport(types: List<ChecksumType>, csFileSelect: PanFileOrDirSelectorPanel, csOut: TextArea) {
        ChecksumReport(object: TextProcessor {
                override fun clear() {
                    Platform.runLater { csOut.text = "" }
                }

                override fun append(value: String) {
                    Platform.runLater { csOut.appendText(value) }
                }
            }
        ).generateReport(csFileSelect.createFile(), types)
    }

    private fun getSelectedTypes(csCheckBoxes: ArrayList<CheckBox>): List<ChecksumType> {
        val rtn: MutableList<ChecksumType> = ArrayList()
        for (cb in csCheckBoxes) {
            if (cb.isSelected) {
                val cst = ChecksumType.find(cb.text)
                if (cst != null) {
                    rtn.add(cst)
                }
            }
        }
        return rtn
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Checksum().go()
        }
    }
}
