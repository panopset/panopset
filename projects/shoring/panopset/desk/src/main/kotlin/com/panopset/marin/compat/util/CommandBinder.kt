package com.panopset.marin.compat.util

import java.io.StringWriter
import java.util.*

abstract class CommandBinder {
    val cmds = ArrayList<PanCmd>()

    protected fun registerCommand(panCmd: PanCmd) {
        cmds.add(panCmd)
        reset()
    }

    private fun reset() {
        prompt = null
    }

    private var prompt: String? = null

    override fun toString(): String {
        if (prompt == null) {
            val sw = StringWriter()
            for (c in cmds) {
                sw.append(c.toString())
                sw.append(" ")
            }
            prompt = sw.toString()
        }
        return prompt as String
    }

    fun findCommand(keyCode: Int): PanCmd {
        val nullPanCmd = PanCmd("", "", -1)
        for (c in cmds) {
            if (c.key == keyCode) {
                return c
            }
        }
        return nullPanCmd
    }
}
