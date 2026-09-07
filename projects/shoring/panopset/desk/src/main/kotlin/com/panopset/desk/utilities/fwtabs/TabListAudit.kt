package com.panopset.desk.utilities.fwtabs

import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import com.panopset.flywheel.ListAudit
import com.panopset.fxapp.*
import com.panopset.fxapp.PanComponentFactory.createPanButton
import com.panopset.fxapp.PanComponentFactory.createPanHBox
import com.panopset.fxapp.PanComponentFactory.createPanLabel
import com.panopset.fxapp.PanComponentFactory.createPanScrollPane
import com.panopset.fxapp.PanComponentFactory.createPanTextArea
import com.panopset.fxapp.PanComponentFactory.createPanVBox
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane

class TabListAudit(val fxDoc: FxDoc) {
    private lateinit var laDirSelect: PanDirSelectorPanel
    private lateinit var laRunButton: Button
    private lateinit var auditTa: TextArea
    fun createTab(): Tab {
        laDirSelect = PanDirSelectorPanel(fxDoc, "ladirselect")
        auditTa = createPanTextArea(fxDoc,"Click the \"Run\" button", "Results will appear here.")
        laRunButton = createPanButton(fxDoc, { process() },
            "_Run", true, "Audit a directory that contains text files of lists.")

        val rtn = FontManagerFX.registerTab(fxDoc, Tab("List Audit"))
        val bp = BorderPane()
        bp.top = createPanVBox(
            createPanLabel(fxDoc,"Select a directory that has list files that you wish to compare.  Then click run to generate a csv formatted report."),
            createPanHBox(
                laDirSelect.pane,
                laRunButton
            )
        )
        bp.center = createPanScrollPane(auditTa)
        rtn.content = bp
        return rtn
    }

    private fun process() {
        val dir = laDirSelect.createDir()
        val listAudit = ListAudit()
        if (!dir.exists()) {
            Logz.errorMsg("Does not exist.", dir)
            return
        }
        if (!dir.isDirectory) {
            Logz.errorMsg("Not a directory.", dir)
            return
        }
        for (f in dir.listFiles()!!) {
            if (f.isFile) {
                listAudit.add(f.name, Fileop.readLines(f))
            }
        }
        auditTa.text = listAudit.reportText
    }
}
