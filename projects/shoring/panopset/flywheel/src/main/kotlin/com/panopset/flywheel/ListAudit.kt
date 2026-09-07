package com.panopset.flywheel

import com.panopset.compat.Stringop.listToString
import java.io.StringWriter
import java.util.*

class ListAudit {
    var map: MutableMap<String, MutableList<String>> = Collections.synchronizedSortedMap(TreeMap())
    var names: MutableSet<String> = Collections.synchronizedSortedSet(TreeSet())
    fun add(name: String, lines: List<String>) {
        if (!names.contains(name)) {
            names.add(name)
        }
        for (line in lines) {
            var listNames = map[line]
            if (listNames == null) {
                listNames = ArrayList()
                listNames.add(name)
                map[line] = listNames
            } else {
                if (!listNames.contains(name)) {
                    listNames.add(name)
                }
            }
        }
    }

    val report: List<String?>
        get() {
            val rtn: MutableList<String?> = ArrayList()
            rtn.add(headerLine)
            for ((key, value) in map) {
                val sw = StringWriter()
                sw.append(key)
                for (name in names) {
                    sw.append(",")
                    if (value.contains(name)) {
                        sw.append("*")
                    }
                }
                rtn.add(sw.toString())
            }
            return rtn
        }
    val reportText: String
        get() = listToString(report)
    private val headerLine: String
        get() {
            val sw = StringWriter()
            for (name in names) {
                sw.append(String.format(",%s", name))
            }
            return sw.toString()
        }
}
