package com.panopset.desk.utilities.fwtabs

import com.panopset.compat.ByLineFilter
import com.panopset.compat.Logz
import com.panopset.compat.Stringop
import com.panopset.compat.Transformer
import com.panopset.fxapp.*
import com.panopset.fxapp.PanComponentFactory.createPanButton
import com.panopset.fxapp.PanComponentFactory.createPanHBox
import com.panopset.fxapp.PanComponentFactory.createPanInputTextFieldHGrow
import com.panopset.fxapp.PanComponentFactory.createPanLabel
import com.panopset.fxapp.PanComponentFactory.createPanTextArea
import com.panopset.fxapp.PanComponentFactory.createPanVBox
import com.panopset.fxapp.PanComponentFactory.createPersistentPanTextArea
import com.panopset.gp.GlobalReplaceProcessor
import com.panopset.gp.PriorAndReplacementLineMustContainFilter
import javafx.application.Platform
import javafx.scene.control.Tab
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import java.io.IOException
import java.io.StringReader
import java.io.StringWriter

class TabGlobalReplace(val fxDoc: FxDoc) {
    private lateinit var grDirOrFileSelect: PanFileOrDirSelectorPanel
    private lateinit var grFromText: TextArea
    private lateinit var grToText: TextArea
    private lateinit var grFromStrings: TextArea
    private lateinit var grToStrings: TextArea
    private lateinit var grExtensions: TextField
    private lateinit var grRegex: TextField
    private lateinit var grPriorLineMustContain: TextField
    private lateinit var grReplacementLineMustContain: TextField

    fun createTab(): Tab {
        grDirOrFileSelect = PanFileOrDirSelectorPanel(fxDoc, "grdirselect")
        grFromStrings = createPersistentPanTextArea(fxDoc, "fxidFromStrings", "Strings to replace.", "List of Strings to replace.")
        grToStrings = createPersistentPanTextArea(fxDoc, "fxidToStrings", "Replacement Strings.", "List of replacement Strings.")
        grFromText = createPersistentPanTextArea(fxDoc, "fxidFromText", "Input text.", "Input text, if file or directory is not selected.")
        grToText = createPanTextArea(fxDoc,"Output text.", "Output text, if file or directory is not selected.")
        grExtensions = createPanInputTextFieldHGrow(fxDoc, "fxidExtensions", "Only select files with these comma separated extensions.",
            "Leave out the leading period, for example \"java,kt\".")
        grRegex = createPanInputTextFieldHGrow(fxDoc, "fxidRegex", "Use this regex to select files.", "File extension selection is ignored, if this field is filled out.")
        grPriorLineMustContain = createPanInputTextFieldHGrow(fxDoc, "fxidPriorLineMustContain",
            "Enter text that the prior line must contain.", "Text that the prior line must contain, if replacement is to be done.")
        grReplacementLineMustContain = createPanInputTextFieldHGrow(fxDoc, "fxidReplacementLineMustContain",
            "Enter text that the replacement line must contain.", "Text that the replacement line must contain, if replacement is to be done.")
        val rtn = FontManagerFX.registerTab(fxDoc, Tab("Global Replace"))
        val bp = BorderPane()
        bp.center = createMainBox()
        rtn.content = bp
        return rtn
    }

    private fun createMainBox(): VBox {
        return createPanVBox(
            grDirOrFileSelect.pane,
            createPanHBox(grFromStrings, grToStrings),
            createPanHBox(grFromText, grToText),
            createPanHBox(
                createPanButton(fxDoc, {doReplaceAll()},
                    "Replace all", false, "Please make sure everything is backed up first, there is no going back."),
                grExtensions),
            grRegex,
            createPanLabel(fxDoc,"Prior line must contain:"),
            createPanHBox(grPriorLineMustContain),
            createPanLabel(fxDoc,"Replacement lines must contain:"),
            createPanHBox(grReplacementLineMustContain))
    }

    private fun doReplaceAll() {
        val searchList = Stringop.stringToList(grFromStrings.text)
        val replaceList = Stringop.stringToList(grToStrings.text)
        val byLineFilter = createByLineFilter(searchList, replaceList)
        if (grDirOrFileSelect.isPopulated()) {
            doReplaceAllFiles(byLineFilter)
        }
        doReplaceAllTextFields(byLineFilter)
    }

    private fun createByLineFilter(searchList: ArrayList<String>, replaceList: ArrayList<String>): ByLineFilter {
        val priorLineMustContain = grPriorLineMustContain.text
        val replacementLineMustContain = grReplacementLineMustContain.text
        return PriorAndReplacementLineMustContainFilter(
            priorLineMustContain, replacementLineMustContain, searchList, replaceList
        )
    }

    private fun doReplaceAllFiles(byLineFilter: ByLineFilter) {
        val te = GlobalReplaceProcessor(
            grDirOrFileSelect.createFile(),
            grExtensions.text, grDirOrFileSelect.isRecursive()
        )
        try {
            te.process(byLineFilter)
        } catch (e: IOException) {
            Logz.errorEx(e)
        }
    }


    private fun doReplaceAllTextFields(byLineFilter: ByLineFilter) {
        val sw = StringWriter()
        Transformer().withByLineFilter(byLineFilter).process(StringReader(grFromText.text), sw)
        Platform.runLater {
            grToText.text = sw.toString()
        }
    }
}
