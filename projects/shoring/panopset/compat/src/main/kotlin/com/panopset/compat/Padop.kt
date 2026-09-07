package com.panopset.compat

import java.io.StringWriter

object Padop {
    fun rightPad(inp: String?, width: Int, padding: Char = ' '): String {
        if (inp == null) {
            return ""
        }
        val sw = StringWriter()
        sw.append(inp)
        var i = inp.length
        while (i++ < width) {
            sw.append(padding)
        }
        return sw.toString()
    }

    fun leftPad(inp: String?, width: Int, padding: Char = ' '): String {
        if (inp == null) {
            return ""
        }
        val sw = StringWriter()
        var i = inp.length
        while (i++ < width) {
            sw.append(padding)
        }
        sw.append(inp)
        return sw.toString()
    }
}
