package com.panopset.gp

import com.panopset.compat.Logz
import java.io.BufferedReader
import java.io.IOException
import java.io.Reader

open class LineIterator(reader: Reader) {
    var lines: MutableList<String> = ArrayList()
    private var i = 0

    init {
        try {
            BufferedReader(reader).use { br ->
                var str = br.readLine()
                while (str != null) {
                    lines.add(str)
                    str = br.readLine()
                }
            }
        } catch (ex: IOException) {
            Logz.errorEx(ex)
        }
    }

    fun next(): String {
        if (hasNext()) {
            return lines[i++]
        }
        return ""
    }

    fun hasNext(): Boolean {
        return i < lines.size
    }
}
