package com.panopset.compat

interface PersistentMap {
    fun put(key: String, value: String)

    fun getMapValue(key: String): String

    fun getMapValue(key: String, dft: String): String

    fun containsKey(key: String): Boolean
}
