package com.panopset.desk.utilities.fwtabs

import com.panopset.fxapp.FontManagerFX
import com.panopset.fxapp.FxDoc
import com.panopset.fxapp.ReturnCharTitledPane
import javafx.scene.control.Tab

class TabGlobalOptions {
    fun createTab(fxDoc: FxDoc): Tab {
        val rtn = FontManagerFX.registerTab(fxDoc, Tab("Options"))
        rtn.content = ReturnCharTitledPane(fxDoc).pane
        return rtn
    }
}
