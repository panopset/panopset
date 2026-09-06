package com.panopset.fxapp

import com.panopset.compat.AlertColor
import com.panopset.compat.findAlertColor
import com.panopset.fxapp.PanComponentFactory.createPanOutputTextField
import com.panopset.fxapp.PanComponentFactory.panDarkTheme
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority

class SteelWrapper(val fxDoc: FxDoc) {
    private var theCurrentColor = findAlertColor(fxDoc.getIntValue(menuBarStatusMessageColorKey))
    private var thePriorMessage = ""
    private var theCurrentMessage = ""
    private val mbStatusMessage: TextField = createPanOutputTextField(fxDoc, "mbStatusMessage", "Use Help->Logs to see all logs.")

    fun setMsg(msg: String) {
        thePriorMessage = theCurrentMessage
        theCurrentMessage = msg
        mbStatusMessage.text = msg
    }

    fun refresh() {
        mbStatusMessage.style = assembleStyle()
    }

    fun updateStyle(alertColor: AlertColor) {
        theCurrentColor = alertColor
        fxDoc.setStringValue(menuBarStatusMessageColorKey, alertColor.ordinal.toString())
        mbStatusMessage.style = assembleStyle()
    }

    fun addToPane(menuBarStatusPane: HBox) {
        mbStatusMessage.isFocusTraversable = false
        mbStatusMessage.isEditable = false
        mbStatusMessage.id = MBSM
        FontManagerFX.register(fxDoc, mbStatusMessage)
        HBox.setHgrow(mbStatusMessage, Priority.ALWAYS)
        menuBarStatusPane.children.add(mbStatusMessage)
    }

    private fun assembleStyle(): String {
        val rootStyle = fxDoc.scene.root.style
        val isDark = if (rootStyle.isEmpty()) {
            false
        } else {
            rootStyle.equals(panDarkTheme)
        }
        val hexFg = if (isDark) theCurrentColor.hexDkFg else theCurrentColor.hexLtFg
        val hexBg = if (isDark) theCurrentColor.hexDkBg else theCurrentColor.hexLtBg
        val fontStyle = FontManagerFX.getStyleFor(FontManagerFX.fontSize.value)
        val rtn =  "${rootStyle}; -fx-background-color: #${hexBg}; -fx-text-fill: #$hexFg $fontStyle"
        return rtn
    }
}

const val menuBarStatusMessageColorKey = "mbAlertColor"
