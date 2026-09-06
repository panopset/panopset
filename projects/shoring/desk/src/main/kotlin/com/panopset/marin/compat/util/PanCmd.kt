package com.panopset.marin.compat.util

import java.io.StringWriter
import java.util.*

class PanCmd(
    val up: String,
    val action: String,
    val key: Int
) {

    override fun toString(): String {
        val sw = StringWriter()
        sw.append(action.uppercase(Locale.getDefault()))
        sw.append("=")
        sw.append(up)
        return sw.toString()
    }
}
