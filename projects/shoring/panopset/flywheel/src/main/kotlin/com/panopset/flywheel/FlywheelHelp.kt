package com.panopset.flywheel

import com.panopset.compat.Cmd80

object FlywheelHelp {
    val c = Cmd80()

    fun outputHelp() {
        generateHelpText()
    }

    private fun generateHelpText() {
        generateHeader()
        generateVariableList()
        generateFooter()
    }

    private fun generateHeader() {
        c.line80()
        c.centerHeader("Panopset Flywheel fw command line help")
        c.line80()
        c.centerHeader("Usage")
        c.line80()
        c.line80("fw <template file> <output directory>")
        c.line80("fw --help")
        c.line80()
    }

    private fun generateVariableList() {
        FlywheelBuilder().inputList(ArrayList()).construct().mapHold.outputVariablesForHelp()
    }

    private fun generateFooter() {
        c.line80()
        c.centerHeader("fw --help")
        c.line80()
        c.line80("To make fw and other Panopset commands available on your system,")
        c.line80("include Panopset software in your path. On Linux:")
        println("/opt/panopset/bin")
        c.line80("")
        c.line80("... on Windows:")
        println("c:\\Program Files\\panopset")
        c.line80("")
        c.line80("... and on MacOS:")
        println("/Applications/panopset.app/Contents/MacOS")
        c.line80("")
        c.line80()
        c.centerHeader("END")
        c.line80()
    }
}
