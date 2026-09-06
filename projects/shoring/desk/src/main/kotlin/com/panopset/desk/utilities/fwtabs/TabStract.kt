package com.panopset.desk.utilities.fwtabs

import com.panopset.compat.Logz
import com.panopset.compat.Stringop.getEol
import com.panopset.fxapp.*
import com.panopset.fxapp.PanComponentFactory.createPanButton
import com.panopset.fxapp.PanComponentFactory.createPanCheckBox
import com.panopset.fxapp.PanComponentFactory.createPanFlowPane
import com.panopset.fxapp.PanComponentFactory.createPanInputTextFieldHGrow
import com.panopset.fxapp.PanComponentFactory.createPanScrollPane
import com.panopset.fxapp.PanComponentFactory.createPanSplitPane
import com.panopset.fxapp.PanComponentFactory.createPanTextArea
import com.panopset.fxapp.PanComponentFactory.createPersistentPanTextArea
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import java.io.BufferedReader
import java.io.IOException
import java.io.StringReader

class TabStract(val fxDoc: FxDoc) {
    private lateinit var stractSrc: TextArea
    private lateinit var stractOut: TextArea
    private lateinit var stractGoButton: Button
    private lateinit var stractFlatten: CheckBox
    private lateinit var stractBreaks: TextField

    fun createTab(): Tab {
        stractFlatten = createPanCheckBox(fxDoc, "stract_flatten", "Flatten", "Place extracted column entries on one line.")
        stractBreaks = createPanInputTextFieldHGrow(fxDoc, "stractBreaks", "Add a return character after each character specified here.", "")
        stractSrc = createPersistentPanTextArea(fxDoc, "stract_src", "Paste something you wish one column out of, here.", "Select a column from the top line, then press the \"Extract\" button.")
        stractOut = createPanTextArea(fxDoc,"Output will appear here.", "See text area at the left for more information.")
        stractGoButton = createPanButton(fxDoc, {
            val i = stractSrc.selection.start
            val j = stractSrc.selection.end
            if (j > i) {
                try {
                    process(i, j)
                } catch (e: IOException) {
                    Logz.errorEx(e)
                }
            }
        }, "_Extract", true, "Extract column from left text area, based on selected text in the top row.")
        val bp = BorderPane()
        bp.top = createPanFlowPane(
            stractGoButton, stractFlatten, stractBreaks,
            createPanButton(fxDoc, {stractSrc.text = ""}, "Clear _input",true, "Clear the input text area."),
            createPanButton(fxDoc, {stractOut.text = ""}, "Clear _output", true, "Clear the output text area.")
        )
        bp.center = createPanSplitPane(fxDoc, "stractSplitLoc",
            createPanScrollPane(stractSrc), createPanScrollPane(stractOut))
        val rtn = FontManagerFX.registerTab(fxDoc, Tab("Stract"))
        rtn.content = bp
        return rtn
    }

    private fun process(startIndex: Int, endIndex: Int) {
        val sr = StringReader(stractSrc.text)
        BufferedReader(sr).use { br ->
            var s = br.readLine()
            while (s != null) {
                val lnth = s.length
                if (lnth > startIndex) {
                    var out = if (endIndex > lnth) {
                        s.substring(startIndex)
                    } else {
                        s.substring(startIndex, endIndex)
                    }
                    if (stractBreaks.text.isNotEmpty()) {
                        out = out.replace(stractBreaks.text, "${stractBreaks}\n")
                    }
                    stractOut.appendText(out)
                    if (!stractFlatten.isSelected) {
                        stractOut.appendText(getEol())
                    }
                }
                s = br.readLine()
            }
        }
    }
}
