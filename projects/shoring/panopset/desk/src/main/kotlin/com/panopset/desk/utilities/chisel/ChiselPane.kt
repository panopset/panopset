package com.panopset.desk.utilities.chisel

import com.panopset.compat.NetworkInfo
import com.panopset.compat.convertBytesToHex
import com.panopset.fxapp.*
import com.panopset.fxapp.PanComponentFactory.createPanButton
import com.panopset.fxapp.PanComponentFactory.createPanCheckBox
import com.panopset.fxapp.PanComponentFactory.createPanHBox
import com.panopset.fxapp.PanComponentFactory.createPanScrollPane
import com.panopset.fxapp.PanComponentFactory.createPanTextArea
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.CheckBox
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import java.io.StringWriter
import java.util.*

class ChiselPane(private val fxDoc: FxDoc) {
    private lateinit var outputTA: TextArea
    private lateinit var verboseCheckBox: CheckBox
    private var lastCommand = LastCommand.NET

    fun createPane(): BorderPane {
        outputTA = createPanTextArea(fxDoc)
        verboseCheckBox = createPanCheckBox(fxDoc, "chiselVBCB", "Verbose", "Expand acronyms in the output.")
        verboseCheckBox.onAction = EventHandler {
            when (lastCommand) {
                LastCommand.NET -> generateNetworkInfo()
                LastCommand.ENV -> generateEnvironmentInfo()
                LastCommand.SYS -> generateSystemInfo()
            }
        }
        val bp = BorderPane()
        bp.top = createControlPane()
        bp.center = createPanScrollPane(outputTA)
        return bp
    }

    private fun createControlPane(): HBox {
        val cp = createPanHBox(
            verboseCheckBox,
            createPanButton(fxDoc, { run { generateNetworkInfo() } } , "Network", false, ""),
            createPanButton(fxDoc, { run { generateEnvironmentInfo() } }, "Environment", false, ""),
            createPanButton(fxDoc, { run { generateSystemInfo() } }, "System", false, "")
        )
        return cp
    }

    private fun generateNetworkInfo() {
        Platform.runLater {
            outputTA.text = NetworkInfo.createNetSummary(verboseCheckBox.isSelected)
        }
        lastCommand = LastCommand.NET
    }

    private fun generateEnvironmentInfo() {
        Platform.runLater {
            outputTA.text = genEnvInfo()
        }
        lastCommand = LastCommand.ENV
    }

    private fun generateSystemInfo() {
        Platform.runLater {
            outputTA.text = genSysInfo()
        }
        lastCommand = LastCommand.SYS
    }

    private fun genEnvInfo(): String {
        val sw = StringWriter()
        for (e in sortMap(System.getenv()).entries) {
            if (verboseCheckBox.isSelected) {
                sw.append("key: ${e.key} value: ${e.value}\n")
            } else {
                sw.append("${e.key}: ${e.value}\n")
            }
        }
        return sw.toString()
    }

    private fun genSysInfo(): String {
        val sw = StringWriter()
        for (e in sortProperties(System.getProperties())) {
            if (verboseCheckBox.isSelected) {
                if (e.key == "line.separator") {
                    sw.append("key: ${e.key} hex value: ${convertBytesToHex(e.value.toByteArray())}\n")
                } else {
                    sw.append("key: ${e.key}: value: ${e.value}\n")
                }
            } else {
                sw.append("${e.key}: ${e.value}\n")
            }
        }
        return sw.toString()
    }

    private fun sortMap(map: Map<String, String>): Map<String, String> {
        val sortedMap = Collections.synchronizedSortedMap<String, String>(TreeMap())
        for (e in map) {
            sortedMap[e.key] = e.value
        }
        return sortedMap
    }

    private fun sortProperties(props: Properties): Map<String, String> {
        val sortedMap = Collections.synchronizedSortedMap<String, String>(TreeMap())
        for (e in props) {
            sortedMap[e.key.toString()] = e.value.toString()
        }
        return sortedMap
    }
}

enum class LastCommand {
    NET, ENV, SYS
}
