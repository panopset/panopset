package com.panopset.flywheel

import com.panopset.compat.Stringop.getNextJvmUniqueIDstr
import com.panopset.compat.Stringop.isPopulated
import com.panopset.flywheel.FlywheelHelp.c
import java.io.StringWriter
import java.util.*
import kotlin.collections.HashMap

class MapHold {
    val mapStack = Stack<NamedMap<String, String>>()

    init {
        pushNewMapStack(getNextJvmUniqueIDstr())
    }

    fun pushNewMapStack(mapStackName: String) {
        mapStack.push(NamedMap(mapStackName))
    }

    fun put(key: String, value: String) {
        mapStack.peek().put(key, value)
    }

    fun get(key: String): String {
        if (mapStack.isEmpty()) {
            throw RuntimeException("mapStack empty, this should be an impossible condition.")
        }
        val stack = Stack<NamedMap<String, String>>()
        for (item in mapStack) {
            stack.push(item)
        }
        while (!stack.isEmpty()) {
            val nm = stack.pop()
            val rtn = nm[key]
            if (rtn != null) {
                return rtn
            }
        }
        if (isPopulated(key)) {
            var rtn = System.getProperty(key)
            if (isPopulated(rtn)) {
                return rtn
            }
            rtn = System.getenv(key)
            if (isPopulated(rtn)) {
                return rtn
            }
        }
        return ""
    }


    fun dump(): String {
        return Javop.dump(mapStack.peek().map)
    }

    fun mergeMap(map: MutableMap<String, String>) {
        mapStack.push(NamedMap(getNextJvmUniqueIDstr(), map))
    }

    fun outputVariablesForHelp() {
        c.line80("")
        for ((i, namedMap) in mapStack.withIndex()) {
            outputNamedMap("Level ${i} Variables", namedMap)
        }
        outputMap("System properties, also available as Flywheel variables",
            System.getProperties().toSortedMap(compareBy<Any> {it.toString()}))
        val envMap = Collections.synchronizedSortedMap(TreeMap<String, String>())
        for (key in System.getenv().keys) {
            envMap[key] = System.getenv()[key] ?:""
        }
        outputMap("Environment variables, also available as Flywheel variables", envMap.toSortedMap())
    }

    private fun outputNamedMap(title: String, namedMap: NamedMap<String, String>) {
        outputMap(title, namedMap.map.toSortedMap())
    }

    private fun outputMap(title: String, map: Map<*, *>) {
        c.line80()
        c.centerHeader(title)
        c.line80()
        c.line80("")
        val sw = StringWriter()
        var firstTime = true
        for (e in map.entries) {
            if (firstTime) {
                firstTime = false
            } else {
                sw.append("\n")
            }
            sw.append(e.key.toString())
            sw.append("=")
            sw.append(e.value.toString())
        }
        println(sw.toString())
        c.line80("")
    }

    /*







    fun mergeMap(map: MutableMap<String, String>) {
        mapStack.push(NamedMap(getNextJvmUniqueIDstr(), map))
    }

        val allValues: Map<String, String?>


        val topMap: MutableMap<String, String>
        get() {
            val namedMap = mapStack.peek()
            return namedMap.map
        }

                get() {
            val rtn = Collections.synchronizedMap(TreeMap<String, String>())
            for (nm in mapStack) {
                for ((key, value) in nm.map) {
                    rtn[key] = value
                }
            }
            return rtn
        }

     */
}
