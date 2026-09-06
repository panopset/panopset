package com.panopset.desk.utilities.fwtabs

import com.panopset.compat.Listop
import com.panopset.compat.Logz
import com.panopset.compat.Stringop
import com.panopset.flywheel.*
import com.panopset.fxapp.*
import com.panopset.fxapp.PanComponentFactory.createPanButton
import com.panopset.fxapp.PanComponentFactory.createPanCheckBox
import com.panopset.fxapp.PanComponentFactory.createPanHBox
import com.panopset.fxapp.PanComponentFactory.createPanInputTextField
import com.panopset.fxapp.PanComponentFactory.createPanInputTextFieldHGrow
import com.panopset.fxapp.PanComponentFactory.createPanLabel
import com.panopset.fxapp.PanComponentFactory.createPanScrollPane
import com.panopset.fxapp.PanComponentFactory.createPanSplitPane
import com.panopset.fxapp.PanComponentFactory.createPanTextArea
import com.panopset.fxapp.PanComponentFactory.createPanVBox
import com.panopset.fxapp.PanComponentFactory.createPersistentPanTextArea
import com.panopset.marin.apps.fw.FxFunctionLoader
import com.panopset.marin.apps.fw.FxSampleLoader
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.SplitPane
import javafx.scene.control.Tab
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import java.io.IOException

class TabFlywheel(fxDoc: FxDoc): SceneUpdater(fxDoc) {
    private lateinit var fwFileSelect: PanFileSelectorPanel
    private lateinit var fwDirSelect: PanDirSelectorPanel
    private lateinit var fwInput: TextArea
    private lateinit var fwTemplate: TextArea
    private lateinit var fwOutput: TextArea
    private lateinit var fwLineBreaks: CheckBox
    private lateinit var fwListBreaks: CheckBox
    private lateinit var fwTokens: TextField
    private lateinit var fwSplitz: TextField
    private lateinit var comboFunctions: ComboBox<FlywheelFunction>
    private lateinit var comboSamples: ComboBox<String>
    private var lineFeedRules = LFR_FLATTEN

    private var input = ""
    private var template = ""
    private var templateFile = ""
    private var targetDir = ""
    private var splitz = ""
    private var lineBreaks = false
    private var listBreaks = false
    private var tokens = ""


    fun createTab(): Tab {
        fwFileSelect = PanFileSelectorPanel(fxDoc, "fwFileselect")
        fwDirSelect = PanDirSelectorPanel(fxDoc, "fwDirselect")
        fwInput = createPersistentPanTextArea(fxDoc, "fwInput", "Input list", "Run the filter or template on this input list.")
        fwTemplate = createPersistentPanTextArea(fxDoc, "fwTemplate", "Please enter a flywheel template here.", "Paste and/or edit your freeform flywheel template here, results will immediately appear on the right.")
        fwOutput = createPanTextArea(fxDoc,"Results will appear here.", "The template to the left is applied to each line in the list to the far left.")
        fwLineBreaks = createPanCheckBox(fxDoc, "fwLineBreaks", "Line breaks", "Uncheck this for a flat line output.")
        fwListBreaks = createPanCheckBox(fxDoc, "fwListBreaks", "List breaks", "Check this for line breaks after each list line output.")
        fwTokens = createPanInputTextField(fxDoc, "fwTokens", "Tokens", "Tokens used to separate input lines. See Tokens, from the Samples dropdown menu, for an example.")
        fwSplitz = createPanInputTextFieldHGrow(fxDoc, "fwSplitz", "Comma separated input line split widths", "See Splitz, from the Samples. Space word separation is ignored, if this is defined.")

        fwInput.textProperty()
            .addListener { _: ObservableValue<out String?>?, _: String?, _: String? -> triggerAnUpdate() }
        fwTemplate.textProperty()
            .addListener { _: ObservableValue<out String?>?, _: String?, _: String? -> triggerAnUpdate() }
        fwTokens.textProperty()
            .addListener { _: ObservableValue<out String?>?, _: String?, _: String? -> triggerAnUpdate() }
        fwSplitz.textProperty()
            .addListener { _: ObservableValue<out String?>?, _: String?, _: String? -> triggerAnUpdate() }

        comboSamples = ComboBox()
        FxSampleLoader().loadUpSamplesComboBox(
            comboSamples, fwInput, fwTemplate, fwLineBreaks,
            fwListBreaks, fwTokens, fwSplitz
        )
        comboFunctions = ComboBox()
        FxFunctionLoader().loadUpFunctions(comboFunctions, fwTemplate)

        val rtn = FontManagerFX.registerTab(fxDoc, Tab("Flywheel"))
        val bp = BorderPane()
        rtn.content = bp
        bp.top = createTopPane()
        bp.center = createCenterPane(fxDoc)
        bp.bottom = createBottomPane()
        rtn.content = bp
        addSomeEventHandlers()
        return rtn
    }

    private fun addSomeEventHandlers() {
        fwListBreaks.onAction = EventHandler {
            doUpdate()
        }
        fwLineBreaks.onAction = EventHandler {
            doUpdate()
        }
    }

    private fun createTopPane(): VBox {
        return createPanVBox(

            createPanHBox(
                createPanLabel(fxDoc,"Template:"),
                fwFileSelect.pane,
            ),

            createPanHBox(
                createPanLabel(fxDoc,"Output:"),
                fwDirSelect.pane,
            )

        )
    }

    private fun createCenterPane(fxDoc: FxDoc): SplitPane {
        return createPanSplitPane(fxDoc, "fwSplitPane",
            createPanScrollPane(fwInput),
            createPanScrollPane(fwTemplate),
            createPanScrollPane(fwOutput)
        )
    }

    private fun createBottomPane(): VBox {
        return createPanVBox(
            createPanHBox(
                createPanVBox(
                    fwLineBreaks, fwListBreaks
                ),
                fwTokens,
                fwSplitz
            ),
            createPanHBox(
                createPanButton(fxDoc, {doClear()}, "Clear", false, "Clear the list, template, and options."),
                createPanButton(fxDoc, {doClearAll()}, "Clear all", false, "Clear the file selection, list, template, and option."),
                createPanButton(fxDoc, {doFilter()}, "Filter", true, ""),
                createPanLabel(fxDoc,"Samples: "),
                comboSamples,
                comboFunctions
            )
        )
    }

    private fun init() {
        Logz.clear()
        templateFile = fwFileSelect.inputFile.text
        targetDir = fwDirSelect.inputFile.text
        input = fwInput.text
        template = fwTemplate.text
        splitz = fwSplitz.text
        lineBreaks = fwLineBreaks.isSelected
        listBreaks = fwListBreaks.isSelected
        tokens = fwTokens.text
        lineFeedRules = LineFeedRules(fwLineBreaks.isSelected, fwListBreaks.isSelected)
    }

    override fun doUpdate() {
        if (
            fwFileSelect.inputFile.text == templateFile &&
            fwDirSelect.inputFile.text == targetDir &&
            fwInput.text == input &&
            fwTemplate.text == template &&
            fwSplitz.text == splitz &&
            fwLineBreaks.isSelected == lineBreaks &&
            fwListBreaks.isSelected == listBreaks &&
            fwTokens.text == tokens) {
            return
        }
        init()
        if (isListHandling) {
            handleList()
        }
    }

    private fun handleList() {
        val wrktxt = fwOutput.text
        fwOutput.text = wrktxt.replace("\n", "")
        try {
            val report = FlywheelListDriver.Builder(Stringop.stringToList(fwInput.text), fwTemplate.text)
                .withLineFeedRules(lineFeedRules).withSplitz(fwSplitz.text)
                .withTokens(fwTokens.text)
                .build()
            val result = report.output
            fwOutput.text = result
        } catch (e: IOException) {
            Logz.errorEx(e)
        }
        if (!lineBreaks) {
            Logz.warn("Line breaks not checked, so output will be on one line.")
        }
    }

    private val isListHandling: Boolean
        get() = (Stringop.isBlank(fwFileSelect.inputFile.text)
                && Stringop.isBlank(fwDirSelect.inputFile.text))
    private fun doClear() {
        fwInput.text = ""
        fwTemplate.text = ""
        comboSamples.selectionModel.select(0)
        comboFunctions.selectionModel.select(0)
    }

    private fun doClearAll() {
        doClear()
        fwFileSelect.clear()
        fwDirSelect.clear()
    }
    private fun doFilter() {
        val input = fwInput.text
        val filter = fwTemplate.text
        fwOutput.text = Listop().filter(input, filter)
    }

}
