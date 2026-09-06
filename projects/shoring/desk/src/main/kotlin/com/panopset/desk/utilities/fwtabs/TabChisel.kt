package com.panopset.desk.utilities.fwtabs

import com.panopset.desk.utilities.chisel.ChiselPane
import com.panopset.fxapp.FontManagerFX
import com.panopset.fxapp.FxDoc
import javafx.scene.control.Tab

class TabChisel {
    fun createTab(fxDoc: FxDoc): Tab {
        val rtn = FontManagerFX.registerTab(fxDoc, Tab("System"))
        rtn.content = ChiselPane(fxDoc).createPane()
        return rtn
    }
}
