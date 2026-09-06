package com.panopset.compat

import java.io.File
import java.io.IOException

/**
 *
 * Some things, like a font size, should persist for all applications.
 *
 */
val pmf = PersistentMapFile(File(HiddenFolder.getFullPathRelativeTo("global.properties")))

private fun saveToFile() {
    pmf.flush()
}

private fun putValue(key: String, value: String) {
    pmf.put( key, value)
}

fun globalPropsFlush() {
    try {
        saveToFile()
    } catch (ex: IOException) {
        Logz.errorEx(ex)
    }
}

fun globalPropsPut(key: String, value: String) {
    try {
        putValue( key, value)
    } catch (ex: IOException) {
        Logz.errorEx(ex)
    }
}

fun globalPropsGet(key: String): String {
    return try {
        pmf.getMapValue(key)
    } catch (e: IOException) {
        Logz.warn("$key not found, ${e.message}")
        ""
    }
}
