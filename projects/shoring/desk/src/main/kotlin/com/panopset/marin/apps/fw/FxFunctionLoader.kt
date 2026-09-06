package com.panopset.marin.apps.fw

import com.panopset.flywheel.FlywheelFunction
import com.panopset.flywheel.ReflectionInvoker.Companion.all
import com.panopset.flywheel.ScriptSecurity
import javafx.event.EventHandler
import javafx.scene.control.ComboBox
import javafx.scene.control.TextArea
import java.util.*

class FxFunctionLoader {
    fun loadUpFunctions(cbFunctions: ComboBox<FlywheelFunction>, fwTemplate: TextArea) {
        populateDropdown(cbFunctions)
        wireUpEvents(cbFunctions, fwTemplate)
    }

    private fun wireUpEvents(cbFunctions: ComboBox<FlywheelFunction>, fwTemplate: TextArea) {
        cbFunctions.onAction = EventHandler { fwTemplate.appendText(cbFunctions.value.example) }
    }

    private fun populateDropdown(cbFunctions: ComboBox<FlywheelFunction>) {
        ScriptSecurity.defineAllowedScriptCalls()
        cbFunctions.items.add(FlywheelFunction())

        val allBut_dt: MutableCollection<FlywheelFunction> = Collections.synchronizedSortedSet(TreeSet())
        for (ff in all) {
            if (!"dt".equals(ff.key, ignoreCase = true)) {
                allBut_dt.add(ff)
            }
        }
        cbFunctions.items.addAll(allBut_dt)
    }
}
