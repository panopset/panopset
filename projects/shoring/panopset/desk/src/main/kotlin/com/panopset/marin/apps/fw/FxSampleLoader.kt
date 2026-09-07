package com.panopset.marin.apps.fw

import com.panopset.flywheel.samples.FlywheelSample
import com.panopset.flywheel.samples.FlywheelSamples
import javafx.event.EventHandler
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.TextArea
import javafx.scene.control.TextField

class FxSampleLoader() {

    fun loadUpSamplesComboBox(comboBox: ComboBox<String>, fwInput: TextArea, fwTemplate: TextArea,
                              fwLineBreaks: CheckBox, fwListBreaks: CheckBox, fwTokens: TextField, fwSplitz: TextField) {
        comboBox.items.add("")
        val flywheelSamples = FlywheelSamples()
        for (fs in flywheelSamples.all()) {
            comboBox.items.add(fs.desc)
        }
        comboBox.onAction = EventHandler {
            val desc = comboBox.value
            val fs: FlywheelSample? =
                flywheelSamples.find(desc)
            if (fs != null) {
                fwInput.text = fs.listText
                fwTemplate.text = fs.templateText
                fwLineBreaks.isSelected = fs.lineBreaks
                fwListBreaks.isSelected = fs.listBreaks
                fwTokens.text = fs.tokens
                fwSplitz.text = fs.splitz
            }
        }
    }
}
