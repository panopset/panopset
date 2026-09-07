package com.panopset.compat

import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.StringWriter

fun panStringToHex(str: String): String {
    val sw = StringWriter()
    for (c in str.chars()) {
        sw.append(panCharToHex(c))
    }
    return sw.toString()
}

fun panCharToHex(c: Int): String {
    return String.format("%02X", c)
}

fun panStringToBackSlashes(str: String): String {
    val sw = StringWriter()
    for (c in str.chars()) {
        val hexRep = panCharToHex(c)
        sw.append(backSlashOf(c, hexRep))
    }
    return sw.toString()
}

fun loadStringToDumpTruck(text: String?, start: Int, max: Int, width: Int,
    isSpace: Boolean
): DumpTruck {
    val baisInp = text ?: ""
    val bais = ByteArrayInputStream(baisInp.toByteArray())
    return loadInputStreamToDumpTruck( bais, start, max, width, isSpace, baisInp.length)
}

fun loadInputStreamToDumpTruck(ip: InputStream, start: Int, max: Int, requestedWidth: Int,
    isSpaces: Boolean, streamLength: Int
): DumpTruck {
    val srcLineList = ArrayList<String>()
    val chrLineList = ArrayList<String>()
    val hexLineList = ArrayList<String>()
    val width = if (requestedWidth < 1) {
        streamLength
    } else {
        requestedWidth
    }
    try {
        val bis = if (ip is BufferedInputStream) {
            ip
        } else {
            BufferedInputStream(ip)
        }
        var i = 0
        var lineByteCount = 0
        if (start > 0) {
            if (start.toLong() > streamLength) {
                return toDumpTruck( "File length of $streamLength is smaller than the start position")
            }
            bis.skip(start.toLong())
        }
        var c = bis.read()
        while (hasMore(i++, max, c)) {
            val srcLineBuffer = StringWriter()
            val chrLineBuffer = StringWriter()
            val hexLineBuffer = StringWriter()
            do {
                if (lineByteCount > 0 && isSpaces) {
                    srcLineBuffer.append(" ")
                    chrLineBuffer.append(" ")
                    hexLineBuffer.append(" ")
                }
                val hexRepOfCurrentChar = panCharToHex(c)
                hexLineBuffer.append(hexRepOfCurrentChar)
                chrLineBuffer.append(backSlashOf(c, hexRepOfCurrentChar))
                c = bis.read()
            } while (++lineByteCount < width && c != -1)
            srcLineList.add(srcLineBuffer.toString())
            chrLineList.add(chrLineBuffer.toString())
            hexLineList.add(hexLineBuffer.toString())
            lineByteCount = 0
        }
    } catch (ex: Exception) {
        throw RuntimeException(ex)
    }
    val sw = StringWriter()
    for (i in srcLineList.indices) {
        sw.append()
    }
    return DumpTruck(srcLineList, chrLineList, hexLineList)
}

fun toDumpTruck(s: String): DumpTruck {
    return DumpTruck( s, "", "")
}

data class DumpTruck(val src: List<String>, val charRep: List<String>, val hexRep: List<String>) {
    constructor(src: String, charRep: String, hexRep: String) : this(
        Stringop.stringToList( src),
        Stringop.stringToList( charRep), Stringop.stringToList( hexRep)
    )
}

private fun hasMore(i: Int, max: Int, c: Int): Boolean {
    return if (c != -1 && max > 0 && i >= max) {
        true
    } else {
        c != -1
    }
}


fun backSlashOf(c: Int, hexRep: String): String {
    val backSlasher = if (!isInBackSlashRange(c)) {
        ""
    } else if ("09" == hexRep) {
        "\\t"
    } else if ("08" == hexRep) {
        "\\b"
    } else if ("0A" == hexRep) {
        "\\n"
    } else if ("0D" == hexRep) {
        "\\r"
    } else {
        if ("0C" == hexRep) "\\f" else ""
    }
    val sw = StringWriter()
    if (backSlasher.isNotEmpty()) {
        sw.append(backSlasher)
    } else {
        sw.append(" ")
        val ch = c.toChar()
        if (Character.isWhitespace(ch)) {
            sw.append(" ")
        } else {
            sw.append(c.toChar().toString())
        }
    }
    return sw.toString()
}

private fun isInBackSlashRange(c: Int): Boolean {
    return c < 14
}
