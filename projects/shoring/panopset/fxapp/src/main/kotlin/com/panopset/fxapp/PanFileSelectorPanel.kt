package com.panopset.fxapp

import com.panopset.compat.Fileop.getCanonicalPath
import com.panopset.compat.Stringop.USH
import com.panopset.fxapp.PanComponentFactory.createPanButton
import com.panopset.fxapp.PanComponentFactory.createPanInputTextFieldHGrow
import com.panopset.fxapp.PanComponentFactory.decorateFileInputTextForMouse
import javafx.scene.control.Tooltip
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import java.io.File

class PanFileSelectorPanel(fxDoc: FxDoc, fxId: String) {
    fun createFile(): File {
        return File(inputFile.text)
    }

    val inputFile = createPanInputTextFieldHGrow(fxDoc, fxId, "Please choose a file.", "Selected file full path.")
    val pane = HBox()

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
        return File(USH)
    }

    fun clear() {
        inputFile.text = ""
    }

    init {
        pane.children.add(createPanButton(fxDoc, {
            val fc = FileChooser()
            fc.initialDirectory = getInitialDirectoryFrom(inputFile.text)
            val file = fc.showOpenDialog(JavaFXapp.findStage())
            if (file.exists() && file.isFile) {
                inputFile.text = getCanonicalPath(file)
                inputFile.tooltip = Tooltip("Selected file full path.")
            }
        }, "File", false, "Select file."))
        HBox.setHgrow(pane, Priority.ALWAYS)
        pane.children.add(inputFile)
        decorateFileInputTextForMouse(inputFile)
    }
}
