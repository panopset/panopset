package com.panopset.fxapp

import com.panopset.compat.*
import com.panopset.compat.Stringop.getEol
import com.panopset.fxapp.PanComponentFactory.createPanHBox
import com.panopset.fxapp.PanComponentFactory.createPanRadioButton
import com.panopset.fxapp.PanComponentFactory.createPanTitledPane
import javafx.event.EventHandler
import javafx.scene.control.ToggleGroup
import javafx.scene.control.Tooltip

class ReturnCharTitledPane(val fxDoc: FxDoc) {
    val pane = createPanTitledPane(fxDoc, "Return Characters")

    private val tg = ToggleGroup()
    private val returnCharactersDoNothing = createPanRadioButton(tg,
        "fxidReturnCharactersDoNothing", fxDoc,
        "Do nothing   ", "System return characters.")
    private val windowsToUnix = createPanRadioButton(tg,
        "fxidWindowsToUnix", fxDoc,
        "lf   ", "UNIX style line breaks, aka \\n.")
    private val unixToWindows = createPanRadioButton(tg,
        "fxidUnixToWindows", fxDoc,
        "crlf   ", "Windows style line breaks, aka \\r\\n.")


    init {
        returnCharactersDoNothing.onAction = EventHandler {
            showUpdate()
        }
        windowsToUnix.onAction = EventHandler {
            showUpdate()
        }
        unixToWindows.onAction = EventHandler {
            showUpdate()
        }
    }

    private fun showUpdate() {
        Stringop.setEol(getSelectedReturnCharacter())
        Logz.info("Selected return character is now ${getSelectionDescription()}")
    }

    private fun getSelectedReturnCharacter(): String {
        if (tg.selectedToggle === returnCharactersDoNothing) {
            return getEol()
        } else if (tg.selectedToggle === windowsToUnix) {
            return "\n"
        } else if (tg.selectedToggle === unixToWindows) {
            return Stringop.DOS_RTN
        }
        return getEol()
    }

    private val cacheMap = HashMap<String, String>()

    private fun getSelectionDescription(): String {
        val rc = getSelectedReturnCharacter()
        return if (cacheMap.containsKey(rc)) {
            cacheMap[rc] ?: createReturnCharacterDescription(rc)
        } else {
            val rtn = createReturnCharacterDescription(rc)
            cacheMap[rc] = rtn
            rtn
        }
    }

    private fun createReturnCharacterDescription(rc: String): String {
        return "\"${panStringToBackSlashes(rc)}\", hex: ${panStringToHex(rc)}."
    }

    init {
        pane.tooltip = Tooltip("Use this control panel to force a specific line separator. If do nothing is selected, the system line separator will be used.")
        pane.content = createPanHBox(returnCharactersDoNothing,
            windowsToUnix, unixToWindows)
        if (!returnCharactersDoNothing.isSelected && !windowsToUnix.isSelected &&
            !unixToWindows.isSelected
        ) {
            returnCharactersDoNothing.isSelected = true
        }
    }
}
