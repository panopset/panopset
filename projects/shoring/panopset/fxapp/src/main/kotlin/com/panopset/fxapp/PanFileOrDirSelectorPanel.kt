package com.panopset.fxapp

import com.panopset.compat.Fileop
import com.panopset.compat.Stringop
import com.panopset.fxapp.PanComponentFactory.createPanButton
import com.panopset.fxapp.PanComponentFactory.createPanCheckBox
import com.panopset.fxapp.PanComponentFactory.createPanInputTextFieldHGrow
import com.panopset.fxapp.PanComponentFactory.decorateFileInputTextForMouse
import javafx.beans.value.ObservableValue
import javafx.scene.control.Tooltip
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import java.io.File

class PanFileOrDirSelectorPanel(fxDoc: FxDoc, fxId: String) {
    fun createFile(): File {
        return File(inputFile.text)
    }

    fun isRecursive(): Boolean {
        return recursiveCheckBox.isSelected
    }

    private val inputFile = createPanInputTextFieldHGrow(fxDoc, fxId,
    "Please select a file or directory.",
        "Selected file or directory full path.")
    val pane = HBox()
    private val recursiveCheckBox = createPanCheckBox(
        fxDoc, "${fxId}_isRecursive", "Recursive ",
        "Traverse subdirectories of selected directory."
    )

    private fun triggerUpdate() {
        val fileChecker = createFile()
        recursiveCheckBox.visibleProperty().value = fileChecker.isDirectory && fileChecker.exists()
    }

    private fun getInitialDirectoryFrom(path: String): File {
        if (inputFile.text.isNotEmpty()) {
            val oldFile = File(path)
            if (oldFile.exists()) {
                return if (oldFile.isFile) {
                    oldFile.parentFile
                } else {
                    oldFile
                }
            }
        }
        return File(Stringop.USH)
    }

    fun isPopulated(): Boolean {
        return inputFile.text.isNotEmpty()
    }

    init {
        inputFile.textProperty()
            .addListener { _: ObservableValue<out String?>?, _: String?, _: String? -> triggerUpdate() }
        recursiveCheckBox.isSelected = true
        pane.children.add(createPanButton(fxDoc, {
            val dc = DirectoryChooser()
            dc.initialDirectory = getInitialDirectoryFrom(inputFile.text)
            val file = dc.showDialog(JavaFXapp.findStage())
            if (file.exists() && file.isDirectory) {
                inputFile.text = Fileop.getCanonicalPath(file)
                inputFile.tooltip = Tooltip("Selected directory full path.")
                recursiveCheckBox.visibleProperty().value = true
            }
        }, "Directory", false, "Select directory"))
        pane.children.add(createPanButton(fxDoc, {
            val fc = FileChooser()
            fc.initialDirectory = getInitialDirectoryFrom(inputFile.text)
            val file = fc.showOpenDialog(JavaFXapp.findStage())
            if (file.exists() && file.isFile) {
                inputFile.text = Fileop.getCanonicalPath(file)
                inputFile.tooltip = Tooltip("Selected file full path.")
                recursiveCheckBox.visibleProperty().value = false
            }
        }, "File", false, "Select file."))
        pane.children.add(recursiveCheckBox)
        HBox.setHgrow(pane, Priority.ALWAYS)
        pane.children.add(inputFile)
        decorateFileInputTextForMouse(inputFile)
    }
}
