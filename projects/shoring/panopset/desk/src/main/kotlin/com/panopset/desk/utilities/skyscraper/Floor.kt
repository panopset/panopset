package com.panopset.desk.utilities.skyscraper

import com.panopset.compat.HttpCommMethod
import com.panopset.flywheel.FlywheelBuilder
import com.panopset.fxapp.*
import com.panopset.fxapp.PanComponentFactory.createPanChoiceBox
import com.panopset.fxapp.PanComponentFactory.createPanInputTextFieldHGrow
import com.panopset.fxapp.PanComponentFactory.createPanOutputTextFieldHGrow
import com.panopset.fxapp.PanComponentFactory.createPanTextArea
import com.panopset.fxapp.PanComponentFactory.createPersistentPanTextArea
import javafx.application.Platform
import javafx.scene.control.TextField
import java.util.*
import kotlin.collections.ArrayList


class Floor(val fxDoc: FxDoc) {
    lateinit var panCheckboxMenu: PanCheckboxMenu
    val urlTextField: TextField = createPanInputTextFieldHGrow(fxDoc, "url",
        "Please enter a URL", "URL of the API you wish to test.")
    val urlOutField: TextField = createPanOutputTextFieldHGrow(fxDoc, "url",
        "URL after ENV (environment) variable substitution.")
    val responseTextArea = createPanTextArea(fxDoc)
    val postMethodChoiceBox = createPanChoiceBox(fxDoc, "postMethod", httpMethods, "")
    val headerTextArea = createPersistentPanTextArea(fxDoc, "headers",
        "Please enter headers.", "Header format is key:value")
    val bodyTextArea = createPersistentPanTextArea(fxDoc, "body",
    "Please enter a BODY for POSTs", "BODY you wish to POST to the API.")
    val envMap: SortedMap<String, MutableMap<String, String>> = Collections.synchronizedSortedMap(TreeMap())
    private var envMapKey = ""

    init {
        urlTextField.textProperty().addListener { _, oldValue, newValue ->
            updateUrlOutTFfromUrlTextField(envMapKey)
            println("textfield changed from $oldValue to $newValue, envMapKey: $envMapKey.")
        }
    }

    fun updateUrlOutTFfromUrlTextField(envMapKey: String) {
        this.envMapKey = envMapKey
        updateUrlOutTFfromFreshValue(envMapKey, urlTextField.text)
    }

    private fun updateUrlOutTFfromFreshValue(envMapKey: String, newValue: String) {
        val props = envMap[envMapKey]
        if (props.isNullOrEmpty()) {
            updateUrlOutTFdirect(newValue)
        } else {
            val fullURL = FlywheelBuilder().inputString(newValue).map(props.toMap()).construct().exec()
            updateUrlOutTFdirect(fullURL)
        }
    }

    fun updateResponse(msg: String) {
        Platform.runLater {
            responseTextArea.text = msg
        }
    }

    private fun updateUrlOutTFdirect(value: String) {
        Platform.runLater {
            urlOutField.text = value
        }
    }
}

private val httpMethods = getHttpMethodEntries()
private fun getHttpMethodEntries(): ArrayList<String> {
    val rtn = ArrayList<String>()
    for (entry in HttpCommMethod.entries) {
        rtn.add(entry.name)
    }
    return rtn
}
