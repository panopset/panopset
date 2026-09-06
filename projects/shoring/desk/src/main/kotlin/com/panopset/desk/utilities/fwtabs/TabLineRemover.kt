package com.panopset.desk.utilities.fwtabs

import com.panopset.compat.deleteLines
import com.panopset.fxapp.*
import com.panopset.fxapp.PanComponentFactory.createPanButton
import com.panopset.fxapp.PanComponentFactory.createPanHBox
import com.panopset.fxapp.PanComponentFactory.createPanLabel
import com.panopset.fxapp.PanComponentFactory.createPanScrollPane
import com.panopset.fxapp.PanComponentFactory.createPersistentPanTextArea
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox

class TabLineRemover(val fxDoc: FxDoc) {
    private lateinit var lrFileOrDirSelect: PanFileOrDirSelectorPanel
    private lateinit var lrRemoveLinesButton: Button
    private lateinit var lrSearchCriteria: TextArea
    fun createTab(): Tab {
        lrFileOrDirSelect = PanFileOrDirSelectorPanel(fxDoc, "lr_file_or_dir_select")
        lrRemoveLinesButton = createPanButton(fxDoc, {doUpdate()}, "Remove lines", false,
            "Please ensure everything is backed up, this is irreversible.")
        lrSearchCriteria = createPersistentPanTextArea(fxDoc, "lr_search_criteria",
            "Text to search for, to determine which lines get deleted, go here.",
            "It is a good idea to back everything up before proceeding."
            )
        val rtn = FontManagerFX.registerTab(fxDoc, Tab("Line Remover"))
        val bp = BorderPane()
        bp.top = createTopVBox()
        bp.center = createPanScrollPane(lrSearchCriteria)
        rtn.content = bp
        return rtn
    }

    private fun createTopVBox(): VBox {
        val rtn = VBox()
        rtn.children.add(createPanHBox(
            lrFileOrDirSelect.pane,
            lrRemoveLinesButton)
        )
        rtn.children.add(createPanLabel(fxDoc,"USE WITH CAUTION, clicking Remove Lines is irreversible."))
        return rtn
    }

    private fun doUpdate() {
        deleteLines(lrFileOrDirSelect.createFile(), lrSearchCriteria.text)
    }
}
