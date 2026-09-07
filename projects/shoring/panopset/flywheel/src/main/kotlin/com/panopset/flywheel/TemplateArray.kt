package com.panopset.flywheel

import java.io.StringWriter

open class TemplateArray : TemplateSource {
    private val ia: Array<String>
    override var line = 0

    constructor(data: List<String>) {
        ia = data.toTypedArray<String>()
    }

    constructor(data: Array<String?>) {
        val convers = ArrayList<String>()
        for (s in data) {
            if (s != null) {
                convers.add(s)
            }
        }
        ia = convers.toTypedArray()
    }

    override val isDone: Boolean
        get() = line >= ia.size

    override fun nextRow(): String {
        return ia[line++]
    }

    override fun reset() {
        line = 0
    }

    override val name: String
        get() = ""
    override val raw: String
        get() {
            val sw = StringWriter()
            for (s in ia) {
                sw.append(s)
                sw.append("\n")
            }
            return sw.toString()
        }
}
