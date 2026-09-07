package com.panopset.desk.utilities.dash

import java.io.FileInputStream
import java.util.Properties

class DashOperatingSystemProperties(val env: String) {
    fun load(): HashMap<String, String> {
        val rtn = HashMap<String, String>()
        val propFileName = "app$env.properties"
        val props = Properties()
        props.load(FileInputStream(propFileName))
        if (props.isEmpty) {
            throw RuntimeException("$env.properties file could not be read")
        }
        for (key in props.keys) {
            rtn[key as String] = props.getProperty(key)
        }
        return rtn
    }
}
