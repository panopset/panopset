package com.panopset.desk.utilities.fwtabs

import com.panopset.compat.*
import com.panopset.fxapp.FontManagerFX
import com.panopset.fxapp.FxDoc
import com.panopset.fxapp.PanComponentFactory.createPanButton
import com.panopset.fxapp.PanComponentFactory.createPanCheckBox
import com.panopset.fxapp.PanComponentFactory.createPanHBox
import com.panopset.fxapp.PanComponentFactory.createPanIntTextFieldWithDefaultValue
import com.panopset.fxapp.PanComponentFactory.createPanScrollPane
import com.panopset.fxapp.PanComponentFactory.createPanTextArea
import com.panopset.fxapp.PanComponentFactory.createPanVBox
import com.panopset.fxapp.PanComponentFactory.createPersistentPanTextArea
import com.panopset.fxapp.PanFileSelectorPanel
import com.panopset.fxapp.SceneUpdater
import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import java.io.File

class TabHexDump(fxDoc: FxDoc): SceneUpdater(fxDoc) {
    private lateinit var hdFileSelectController: PanFileSelectorPanel
    private lateinit var hdSpace: CheckBox
    private lateinit var hdChars: CheckBox
    private lateinit var hdStart: TextField
    private lateinit var hdMax: TextField
    private lateinit var hdWidth: TextField
    private lateinit var hdText: TextArea
    private lateinit var hdDump: TextArea
    private lateinit var hdLoad: Button

    fun createTab(): Tab {
        hdFileSelectController = PanFileSelectorPanel(fxDoc, "hd_fileselect")
        hdSpace = createPanCheckBox(fxDoc, "hd_space", "Space   ")
        hdChars = createPanCheckBox(fxDoc, "hd_chars", "Show character line.")
        hdLoad = createPanButton(fxDoc, {loadFile(hdFileSelectController.createFile())},
            "Load", true, "Load bytes from selected file."
        )
        hdStart = createPanIntTextFieldWithDefaultValue(fxDoc, "hd_start", "0",
            "Start", "File start position.")
        hdMax = createPanIntTextFieldWithDefaultValue(fxDoc, "hd_max", "0",
            "Max", "Maximum characters to dump.")
        hdWidth = createPanIntTextFieldWithDefaultValue(fxDoc, "hd_width", "0",
            "Width", "Dump width.")
        hdText = createPersistentPanTextArea(fxDoc, "hd_text", "Text", "Input text to dump.")
        hdDump = createPanTextArea(fxDoc,"Dump", "Dump of text to the left will appear here when you click the dump button.")

        hdText.textProperty()
            .addListener { _: ObservableValue<out String?>?, _: String?, _: String? -> triggerAnUpdate() }
        hdSpace.onAction = EventHandler { triggerAnUpdate() }
        hdChars.onAction = EventHandler { triggerAnUpdate() }

        val rtn = FontManagerFX.registerTab(fxDoc, Tab("Hex Dump"))
        val bp = BorderPane()
        bp.top = createTopPane()
        bp.center = createCenterPane()
        rtn.content = bp
        return rtn
    }

    private fun createTopPane(): VBox {
        return createPanVBox(
            createPanHBox(hdFileSelectController.pane, hdLoad),
            createPanHBox(hdSpace, hdChars, hdStart, hdMax, hdWidth)
        )
    }

    private fun createCenterPane(): SplitPane {
        val rtn = SplitPane()
        BorderPane.setAlignment(rtn, Pos.CENTER)
        rtn.items.add(createPanScrollPane(hdText))
        rtn.items.add(createPanScrollPane(hdDump))
        return rtn
    }

    private fun loadFile(
        file: File
    ) {
        if (!file.exists()) {
            Logz.warn(Nls.xlate("File does not exist."))
            return
        }
        Platform.runLater {
            hdText.text = Fileop.readTextFile(file)
        }
    }

    override fun doUpdate() {
        val dumpTruck = loadStringToDumpTruck(
            hdText.text, Stringop.parseInt(hdStart.text), Stringop.parseInt(hdMax.text),
            Stringop.parseInt(hdWidth.text), hdSpace.isSelected)
        hdDump.text = ""
        for (i in dumpTruck.src.indices) {
            if (hdChars.isSelected) {
                hdDump.appendText(dumpTruck.charRep[i])
                hdDump.appendText("\n")
            }
            hdDump.appendText(dumpTruck.hexRep[i])
            hdDump.appendText("\n")
        }
    }
}
