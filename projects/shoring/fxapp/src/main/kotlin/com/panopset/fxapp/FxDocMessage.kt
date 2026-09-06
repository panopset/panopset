package com.panopset.fxapp

import com.panopset.compat.AlertColor
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority

class FxDocMessage(val fxDoc: FxDoc) {
    private val steelWrapper = SteelWrapper(fxDoc)
    fun setMsg(msg: String) {
        setText(msg)
        steelWrapper.updateStyle(AlertColor.GRN)
    }
    fun setErrorMsg(msg: String) {
        setText(msg)
        steelWrapper.updateStyle(AlertColor.RED)
    }
    fun refresh() {
        steelWrapper.refresh()
    }
    private fun setText(msg: String) {
        steelWrapper.setMsg(msg)
        appendToLogFile(fxDoc, "\n$msg")
        updateLogTA(fxDoc)
    }
    fun createMenuBarStatusPane(): HBox {
        val menuBarStatusPane = HBox()
        HBox.setHgrow(menuBarStatusPane, Priority.ALWAYS)
        steelWrapper.addToPane(menuBarStatusPane)
        return menuBarStatusPane
    }
}
