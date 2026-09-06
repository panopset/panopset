package com.panopset.fxapp

import javafx.scene.control.Menu

abstract class PanCheckboxMenu(name: String) {
    abstract fun isMatch(panCheckboxMenuItem: PanCheckboxMenuItem, value: String): Boolean

    fun setTheCurrentSelection(value: String) {
        currentSelection = value
        for (panCheckboxMenuItem in panCheckboxMenuItems) {
            panCheckboxMenuItem.checkboxMenuItem.isSelected = isMatch(panCheckboxMenuItem, currentSelection)
        }
    }

    val menu = Menu(name)
    val panCheckboxMenuItems = ArrayList<PanCheckboxMenuItem>()
    var currentSelection = ""
}
