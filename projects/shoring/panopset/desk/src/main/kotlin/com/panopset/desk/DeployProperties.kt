package com.panopset.desk

import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import java.io.File
import java.util.Properties

class DeployProperties() {
    private var props: Properties

    init {
        var file = File(deployPropertiesFileName)
        if (!file.exists()) {
            // If we are running this from a JUnit, we'll be at the shoring/desk level.
            file = File("../../$deployPropertiesFileName")
        }
        props = Fileop.loadProps( file)
    }
    fun getPanopsetVersion(): String {
        return props.getProperty(PV, "")?:""
    }

    fun get(key: String): String {
        return props.getProperty(key, "")?:""
    }
}

private const val deployPropertiesFileName = "deploy.properties"
private const val PV = "PV"
