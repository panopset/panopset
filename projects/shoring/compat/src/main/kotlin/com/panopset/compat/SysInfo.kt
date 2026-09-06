package com.panopset.compat

import com.panopset.compat.Stringop.getCommaString
import java.io.StringWriter
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*

/**
 * General system information.
 *
 */
object SysInfo {
    val map: SortedMap<String, String> = Collections.synchronizedSortedMap(TreeMap())
    private val sortByKeyName = Comparator<String> { a, b ->
        when {
            a == null && b == null -> 0
            a == null -> -1
            b == null -> 1
            a.isNotEmpty() && b.isNotEmpty() -> customCompare(a, b)
            else -> 1
        }
    }

    init {
        var name = "Un-able to determine, see log."
        try {
            name = InetAddress.getLocalHost().canonicalHostName
        } catch (e: UnknownHostException) {
            Logz.errorEx(e)
        }
        val runtime = Runtime.getRuntime()
        addValueToMapIfExists(map, "HostName", name)
        addValueToMapIfExists(map, "Processors", getCommaString(runtime.availableProcessors()))
        addValueToMapIfExists(map, "Total memory", getCommaString(runtime.totalMemory()))
        addValueToMapIfExists(map, "Max memory", getCommaString(runtime.maxMemory()))
        addValueToMapIfExists(map, "Free memory", getCommaString(runtime.freeMemory()))
        addSysPropValueToMapIfExists(map, "java.version")
        addSysPropValueToMapIfExists(map, "os.name")
        addSysPropValueToMapIfExists(map, "sun.os.patch.level")
        addSysPropValueToMapIfExists(map, "os.arch")
        addSysPropValueToMapIfExists(map, "sun.arch.data.model")
        addSysPropValueToMapIfExists(map, "sun.cpu.isalist")
        addValueToMapIfExists(map, "Language", Locale.getDefault().language)
    }

    private fun customCompare(a: String, b: String): Int {
        if (a.lowercase().contains("memory") && !b.lowercase().contains("memory")) {
            return -1
        }
        return a.compareTo(b)
    }

    override fun toString(): String {
        return map2string(map.toSortedMap(sortByKeyName))
    }
}

fun addSysPropValueToMapIfExists(map: SortedMap<String, String>, key: String) {
    addValueToMapIfExists(map, key, System.getProperty(key)?: "")
}

fun addValueToMapIfExists(map: SortedMap<String, String>, key: String, value: String) {
    if (value.isBlank()) {
        return
    }
    map[key] = value
}

fun map2string(map: Map<String, String>): String {
    val sw = StringWriter()
    var maxKeyLength = 0
    var maxValueLength = 0
    for (entry in map) {
        if (entry.key.length > maxKeyLength) {
            maxKeyLength = entry.key.length
        }
        if (entry.value.length > maxValueLength) {
            maxValueLength = entry.value.length
        }
    }
    for (entry in map) {
        sw.append(entry.key.padEnd(maxKeyLength, ' '))
        sw.append(" = ")
        val ev = entry.value
        if (ev.contains(",")) {
            sw.append(entry.value.padStart(maxValueLength, ' '))
        } else {
            sw.append(entry.value)
        }
        sw.append("\n")
    }
    return sw.toString()
}
