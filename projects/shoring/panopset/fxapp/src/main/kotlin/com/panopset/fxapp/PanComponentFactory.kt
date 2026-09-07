package com.panopset.fxapp

import com.panopset.compat.Logz
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.*

object PanComponentFactory {
    fun decorateFileInputTextForMouse(inputFile: TextField) {
        inputFile.onDragOver = EventHandler { event: DragEvent ->
            if (event.gestureSource !== inputFile && event.dragboard.hasFiles()) {
                event.acceptTransferModes(*TransferMode.COPY_OR_MOVE)
            }
            event.consume()
        }
    }

    fun createPanIntTextFieldWithDefaultValue(fxDoc: FxDoc, id: String, defaultValue: String,
                                              prompt: String, ttt: String): TextField {
        val rtn = createPanInputTextFieldWithDefaultValue(fxDoc, id, defaultValue, prompt, ttt)
        rtn.textProperty().addListener { _: ObservableValue<out String>?, oldValue: String?, newValue: String ->
            if (newValue.matches("-?([0-9]*)?".toRegex())) return@addListener
            rtn.text = oldValue
        }
        return rtn
    }

    fun createPanInputTextFieldWithDefaultValue(fxDoc: FxDoc, id: String, defaultValue: String,
                                                prompt: String, ttt: String): TextField {
        fxDoc.setDefaultValue(id, defaultValue)
        val rtn = createPanInputTextField(fxDoc, id, prompt, ttt)
        rtn.text = defaultValue
        return rtn
    }

    fun createPanInputTextField(fxDoc: FxDoc, id: String, prompt: String, ttt: String): TextField {
        val rtn = TextField()
        FontManagerFX.register(fxDoc, rtn)
        fxDoc.registerTextInputControl(id, rtn)
        rtn.promptText = prompt
        rtn.tooltip = Tooltip(ttt)
        return rtn
    }

    fun createPanOutputTextField(fxDoc: FxDoc, id: String, ttt: String): TextField {
        val rtn = TextField()
        FontManagerFX.register(fxDoc, rtn)
        fxDoc.registerTextInputControl(id, rtn)
        rtn.tooltip = Tooltip(ttt)
        rtn.isEditable = false
        return rtn
    }

    fun createPanInputTextFieldHGrow(fxDoc: FxDoc, id: String, prompt: String, ttt: String): TextField {
        val rtn = createPanInputTextField(fxDoc, id, prompt, ttt)
        HBox.setHgrow(rtn, Priority.ALWAYS)
        return rtn
    }

    fun createPanInputTextFieldHGrowDft(fxDoc: FxDoc, id: String, prompt: String, ttt: String,
                                        defaultValue: String): TextField {
        val rtn = createPanInputTextFieldHGrow(fxDoc, id, prompt, ttt)
        rtn.text = defaultValue
        return rtn
    }

    fun createPanOutputTextFieldHGrow(fxDoc: FxDoc, id: String, ttt: String): TextField {
        val rtn = createPanOutputTextField(fxDoc, id, ttt)
        HBox.setHgrow(rtn, Priority.ALWAYS)
        return rtn
    }

    fun createPanTextArea(fxDoc: FxDoc, prompt: String, ttt: String): TextArea {
        val rtn = createPanTextArea(fxDoc)
        rtn.promptText = prompt
        rtn.tooltip = Tooltip(ttt)
        return rtn
    }

    fun createPanTextArea(fxDoc: FxDoc, prompt: String, ttt: String, dft: String): TextArea {
        val rtn = createPanTextArea(fxDoc, prompt, ttt)
        rtn.text = dft
        return rtn
    }

    fun createPersistentPanTextArea(fxDoc: FxDoc, id: String, prompt: String, ttt: String, dft: String): TextArea {
        val rtn = createPanTextArea(fxDoc, id, prompt, ttt)
        rtn.text = dft
        return rtn
    }

    fun createPersistentPanTextArea(fxDoc: FxDoc, id: String, prompt: String, ttt: String): TextArea {
        val rtn = createPanTextArea(fxDoc)
        fxDoc.registerTextInputControl(id, rtn)
        rtn.promptText = prompt
        rtn.tooltip = Tooltip(ttt)
        return rtn
    }

    fun createPanTextArea(fxDoc: FxDoc): TextArea {
        val rtn = TextArea()
        FontManagerFX.register(fxDoc, rtn)
        rtn.maxWidth = Double.MAX_VALUE;
        rtn.maxHeight = Double.MAX_VALUE;
        HBox.setHgrow(rtn, Priority.ALWAYS)
        VBox.setVgrow(rtn, Priority.ALWAYS)
        return rtn
    }

    fun createPanCheckBox(fxDoc: FxDoc, id: String, text: String, ttt: String, dft: Boolean): CheckBox {
        val rtn = createPanCheckBox(fxDoc, id, text, ttt)
        rtn.isSelected = dft
        return rtn
    }

    fun createPanCheckBox(fxDoc: FxDoc, id: String, text: String, ttt: String): CheckBox {
        val rtn = createPanCheckBox(fxDoc, id, text)
        rtn.tooltip = Tooltip(ttt)
        return rtn
    }

    fun createPanCheckBox(fxDoc: FxDoc, id: String, text: String): CheckBox {
        val rtn = CheckBox(text)
        FontManagerFX.register(fxDoc, rtn)
        fxDoc.registerCheckBox(id, rtn)
        return rtn
    }

    fun createPanChoiceBox(fxDoc: FxDoc, id: String, choices: ArrayList<String>, defaultValue: String): PanChoiceBox {
        return PanChoiceBox(fxDoc, id, choices, defaultValue)
    }

    fun createPanPwdField(fxDoc: FxDoc): PasswordField {
        val rtn = PasswordField()
        FontManagerFX.register(fxDoc, rtn)
        return rtn
    }

    fun createPanStackPane(vararg nodes: Node): StackPane {
        val rtn = StackPane()
        for (node in nodes) {
            rtn.children.add(node)
        }
        HBox.setHgrow(rtn, Priority.ALWAYS)
        return rtn
    }

    fun createPanTransientField(fxDoc: FxDoc): TextField {
        val rtn = TextField()
        FontManagerFX.register(fxDoc, rtn)
        return rtn
    }

    fun createPanLabel(fxDoc: FxDoc, text: String): Label {
        val rtn = Label(text)
        FontManagerFX.register(fxDoc, rtn)
        return rtn
    }

    fun createPanButton(fxDoc: FxDoc, doRequestedAction: () -> Unit, text: String, mp: Boolean, ttt: String): Button {
        val button = Button()
        button.onAction = EventHandler {
            Logz.setAppLogger(fxDoc)
            doRequestedAction()
        }
        button.text = text
        button.tooltip = Tooltip(ttt)
        button.isMnemonicParsing = mp
        FontManagerFX.register(fxDoc, button)
        return button
    }

    fun createPanFlowPane(vararg nodes: Node): FlowPane {
        val rtn = FlowPane()
        for (node in nodes) {
            rtn.children.add(node)
        }
        return rtn
    }

    fun createPanSplitPane(fxDoc: FxDoc, id: String, vararg nodes: Node): SplitPane {
        val rtn = SplitPane()
        rtn.setDividerPositions(0.20, 0.60)
        for (node in nodes) {
            rtn.items.add(node)
        }
        fxDoc.registerSplitPaneLocations(id, rtn)
        return rtn
    }

    fun createPanHBox(vararg nodes: Node): HBox {
        val rtn = HBox()
        for (node in nodes) {
            rtn.children.add(node)
        }
        return rtn
    }

    fun createPanVBoxHGrow(vararg nodes: Node): VBox {
        val rtn = VBox()
        for (node in nodes) {
            rtn.children.add(node)
        }
        HBox.setHgrow(rtn, Priority.ALWAYS)
        return rtn
    }

    fun createPanVBox(vararg nodes: Node): VBox {
        val rtn = VBox()
        for (node in nodes) {
            rtn.children.add(node)
        }
        return rtn
    }

    fun createPanScrollPane(textArea: TextArea): ScrollPane {
        val rtn = ScrollPane()
        rtn.fitToHeightProperty().value = true
        rtn.fitToWidthProperty().value = true
        rtn.content = textArea
        return rtn
    }

    fun createPanTitledPane(fxDoc: FxDoc, title: String): TitledPane {
        val rtn = TitledPane()
        FontManagerFX.register(fxDoc, rtn)
        rtn.text = title
        rtn.animatedProperty().value = false
        rtn.collapsibleProperty().value = false
        return rtn
    }

    fun createPanTitledPane(fxDoc: FxDoc, title: String, content: Node): TitledPane {
        val rtn = createPanTitledPane(fxDoc, title)
        rtn.content = content
        return rtn
    }

    fun createPanRadioButton(
        tg: ToggleGroup, fxId: String, fxDoc: FxDoc,
        text: String,
        ttt: String
    ): RadioButton {
        val rtn = RadioButton(text)
        FontManagerFX.register(fxDoc, rtn)
        fxDoc.registerToggleButton(fxId, rtn)
        rtn.tooltip = Tooltip(ttt)
        tg.toggles.add(rtn)
        return rtn
    }

    fun createPanTabPane(fxDoc: FxDoc, id: String): TabPane {
        val rtn = TabPane()
        rtn.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        FontManagerFX.register(fxDoc, rtn)
        fxDoc.registerTabSelected(id, rtn)
        return rtn
    }

    const val licenseLinkText = "https://github.com/panopset/panopset/blob/main/LICENSE"
    const val panDarkTheme = "-fx-base:black"
}
