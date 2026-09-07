package com.panopset.compat

import com.panopset.compat.Stringop.parseInt

class Splitter {
    private var i = 0
    private val w: IntArray

    constructor(widths: List<Int>) {
        val ints = IntArray(widths.size)
        for (x in widths) {
            ints[i++] = x
        }
        w = ints
    }

    constructor(vararg widths: Int) {
        w = widths
    }

    fun split(s: String?): List<String> {
        val rtn: MutableList<String> = ArrayList()
        // TODO: Wierd how it flags the more elegant empty while, and doesn't flag this clunky way of doing it.
        var done = recursivelyAccumulate(rtn, s)
        while (!done) {
            done = recursivelyAccumulate(rtn, s)
        }
        return rtn
    }

    private fun recursivelyAccumulate(list: MutableList<String>, s: String?): Boolean {
        if (s == null) {
            return true
        }
        val nextWidth = nextWidth
        if (nextWidth == 0) {
            return true
        }
        if (s.length <= nextWidth) {
            list.add(s)
            return true
        }
        list.add(s.take(nextWidth))
        return recursivelyAccumulate(list, s.substring(nextWidth))
    }

    private val nextWidth: Int
        get() {
            if (i > w.size - 1) {
                i = 0
            }
            return w[i++]
        }
}

fun fixedLengths(commaSeparatedLineSplitWidths: String): Splitter {
    if (commaSeparatedLineSplitWidths.contains(",")) {
        val splitWidths: MutableList<Int> = ArrayList()
        for (splitWidthStr in commaSeparatedLineSplitWidths.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()) {
            splitWidths.add(parseInt( splitWidthStr))
        }
        return Splitter(splitWidths)
    }
    try {
        val splitInt = parseInt(commaSeparatedLineSplitWidths)
        return Splitter(splitInt)
    } catch(e: Exception) {
        Logz.errorEx(e)
        return Splitter(0)
    }
}

fun fixedLength(lineSplitWidth: Int): Splitter {
    return Splitter(lineSplitWidth)
}

fun fixedLengths(vararg lineSplitWidths: Int): Splitter {
    return Splitter(*lineSplitWidths)
}
