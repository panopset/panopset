package com.panopset.fxapp

abstract class PanCheckboxMenuFactory(val name: String) {
    private val panCheckboxMenu = object: PanCheckboxMenu(name) {
        override fun isMatch(panCheckboxMenuItem: PanCheckboxMenuItem, value: String): Boolean {
            return isThisMatch(panCheckboxMenuItem, value)
        }
    }
    private val menuItemNames = ArrayList<String>()

    fun construct(fxDoc: FxDoc): PanCheckboxMenu {
        val docKeyIfNotGlobal = "panCheckboxMenu$name"




        val inival = fxDoc.pmf.getMapValue(docKeyIfNotGlobal)




        val rtn = constructWithInitialValue(inival)





        fxDoc.registerData(docKeyIfNotGlobal,  BoltBox(object : Bolt {
            override fun getBoltValue(): String {
                return rtn.currentSelection
            }

            override fun getBoltDefault(): String {
                return ""
            }

            override fun setBoltValue(value: String) {
                rtn.setTheCurrentSelection(value)
            }
        }))
        return rtn
    }

    fun constructWithInitialValue(initialSelection: String): PanCheckboxMenu {
        populateListOfMenuItemNames(menuItemNames)
        for (name in menuItemNames) {
            panCheckboxMenu.panCheckboxMenuItems.add(PanCheckboxMenuItem(name))
        }
        for (panCheckboxMenuItem in panCheckboxMenu.panCheckboxMenuItems) {
            assignAction(panCheckboxMenuItem, panCheckboxMenu)
            panCheckboxMenu.menu.items.add(panCheckboxMenuItem.checkboxMenuItem)
        }
        panCheckboxMenu.setTheCurrentSelection(initialSelection)
        return panCheckboxMenu
    }

    abstract fun assignAction(panCheckboxMenuItem: PanCheckboxMenuItem, panCheckboxMenu: PanCheckboxMenu)

    abstract fun isThisMatch(panCheckboxMenuItem: PanCheckboxMenuItem, value: String): Boolean

    abstract fun populateListOfMenuItemNames(menuItemNames: ArrayList<String>)
}
