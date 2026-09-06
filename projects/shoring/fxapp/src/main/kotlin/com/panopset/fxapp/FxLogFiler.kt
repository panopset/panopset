package com.panopset.fxapp

import com.panopset.compat.Logz
import com.panopset.compat.SysInfo
import com.panopset.compat.Zombie
import com.panopset.fxapp.JavaFXapp.dds
import com.panopset.fxapp.PanComponentFactory.createPanButton
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.stage.Stage

private val logTextMap = HashMap<FxDoc, FxLogStageDisplayElements>()

fun findLogTextArea(fxDoc: FxDoc): TextArea {
    return findLogStageDisplayElements(fxDoc).textArea
}
//
fun findLogStage(fxDoc: FxDoc): Stage {
    return findLogStageDisplayElements(fxDoc).logStage
}

private fun findLogStageDisplayElements(fxDoc: FxDoc): FxLogStageDisplayElements {
    if (logTextMap.containsKey(fxDoc)) {
        val logTextMap = logTextMap[fxDoc]
        if (logTextMap != null) {
            return logTextMap
        }
    }
    val fxLogStageDisplayElements = FxLogStageDisplayElements()
    logTextMap[fxDoc] = fxLogStageDisplayElements
    fxLogStageDisplayElements.textArea = TextArea()
    fxLogStageDisplayElements.logStage = createLogStage(fxDoc, fxLogStageDisplayElements.textArea)
    return fxLogStageDisplayElements
}

fun createLogStage(fxDoc: FxDoc, logTa: TextArea): Stage {
    val rtn = Stage()
    rtn.title = "Logs"

    val borderPane = BorderPane()

    logTa.promptText = "Click refresh to load log from file."
    val clearLog = createPanButton(
        fxDoc, {
            clearLogFileContents(fxDoc)
            Logz.clear()
            Platform.runLater {
                logTa.text = ""
            }
        },
        "Clear", false, "Clear logs."
    )
    val refreshLog = createPanButton(
        fxDoc,
        { updateLogTA(fxDoc) },
        "Refresh", false, "Refresh log."
    )

    FontManagerFX.register(fxDoc, logTa)

    val topFlow = FlowPane()

    topFlow.children.add(refreshLog)
    topFlow.children.add(clearLog)
    topFlow.children.add(createPanButton(fxDoc, {
        logTa.text = SysInfo.toString()
    }, "System", false, ""))
    borderPane.top = topFlow


    val scrollPane = ScrollPane()
    scrollPane.fitToHeightProperty().value = true
    scrollPane.fitToWidthProperty().value = true
    scrollPane.content = logTa
    borderPane.center = scrollPane
    updateLogTA(fxDoc)
    Zombie.addStopAction {
        rtn.close()
    }
    rtn.scene = Scene(borderPane, 600.0, 400.0)
    rtn.icons.add(dds.createFaviconImage())
    return rtn
}

fun updateLogTA(fxDoc: FxDoc) {
    val contents = getLogFileContents(fxDoc)
    Platform.runLater {
        findLogTextArea(fxDoc).text = contents
    }
}
