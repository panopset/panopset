package com.panopset.fxapp

import com.panopset.compat.Logz
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.control.ChoiceBox
import java.util.ArrayList

class PanChoiceBox(fxDoc: FxDoc, id: String, choices: kotlin.collections.ArrayList<String>, defaultValue: String) {
    private val choiceBox = ChoiceBox<String>()

    init {
        FontManagerFX.register(fxDoc, choiceBox)
        fxDoc.registerChoiceBox(id, this, defaultValue, choices)
        setChoiceBoxChoices(choiceBox, choices)
        choiceBox.value = defaultValue
    }

    fun getCurrentChoice(): String {
        return choiceBox.value
    }

    fun getChoiceBoxForDisplayOnly(): ChoiceBox<String> {
        return choiceBox
    }

    fun register(anchor: Anchor, keyChain: String, defaultValue: String, choices: ArrayList<String>) {
        anchor.registerData(keyChain, BoltBox(object: Bolt {
            override fun getBoltValue(): String {
                return choiceBox.value ?: defaultValue
            }

            override fun getBoltDefault(): String {
                return defaultValue
            }

            override fun setBoltValue(value: String) {
                if (choices.contains(value)) {
                    choiceBox.selectionModel.select(value)
                } else {
                    Logz.warn("$value is not a valid choice for $keyChain")
                }
            }
        }))
    }

    fun setAction(handler: EventHandler<javafx.event.ActionEvent>) {
        choiceBox.onAction = handler
    }
}

private fun setChoiceBoxChoices(ch: ChoiceBox<String>, choices: ArrayList<String>) {
    val ol = FXCollections.observableArrayList(choices)
    ch.items = ol
    val selected = ch.selectionModel.selectedIndex
    if (selected == -1) {
        ch.selectionModel.select(0)
    }
}
