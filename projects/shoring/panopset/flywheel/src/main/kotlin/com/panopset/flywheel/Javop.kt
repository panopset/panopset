package com.panopset.flywheel

import com.panopset.compat.Nls
import com.panopset.compat.Stringop.getEol
import java.io.StringWriter

object Javop {
    fun copyMap(
        fromMap: Map<String, String>,
        toMap: MutableMap<String, String>
    ): Map<String, String> {
        for (e in fromMap) {
            toMap[e.key] = e.value
        }
        return toMap
    }

    fun copyMapObjects(
        fromMap: Map<String, Any>,
        toMap: MutableMap<String, String>
    ): Map<String, String> {
        for (e in fromMap) {
            toMap[e.key] = e.value.toString()
        }
        return toMap
    }

    fun dump(obj: Any?): String {
        val sw = StringWriter()
        if (obj == null) {
            return Nls.xlate("Can not dump null object") + "."
        }
        if (obj is Array<*> && obj.isArrayOf<Any>()) {
            for (s: Any? in obj) {
                sw.append(dump(s))
            }
        } else if (obj is Map<*, *>) {
            for (e in obj) {
                sw.append(e.key.toString())
                sw.append(getEol())
                val v: Any? = e.value
                if (v == null) {
                    sw.append("null")
                } else {
                    sw.append(v.toString())
                }
                sw.append(getEol())
                sw.append(getEol())
            }
        } else if (obj is Collection<*>) {
            for (e: Any? in obj) {
                sw.append(dump(e))
            }
        } else {
            sw.append(obj.toString())
            sw.append(getEol())
        }
        return sw.toString()
    }
}
