package com.panopset.compat

import com.panopset.compat.Fileop.touch
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Propop {
    fun loadPropsFromFile(file: File): Map<String, String> {
        val props = Properties()
        load( props, file)
        return loadMapFromProperties(props)
    }

    fun loadMapFromProperties(properties: Properties): Map<String, String> {
        val rtn: MutableMap<String, String> = HashMap()
        for (key in properties.keys) {
            rtn[key.toString()] = properties.getProperty(key.toString())
        }
        return rtn
    }

    /**
     * If file can not be read, an empty Properties object is returned.
     *
     * @param props Properties to load.
     * @param file File to load properties from.
     *
     * @throws IOException exception.
     */
    fun load(props: Properties, file: File): Properties {
        val rtn: Properties = props
        if (!file.exists()) {
            touch( file)
        }
        try {
            FileReader(file).use { fr -> rtn.load(fr) }
        } catch(rte: RuntimeException) {
            Logz.errorMsg(file, rte)
            return rtn
        }
        return rtn
    }

    /**
     * If file can not be read, an empty Properties object is returned.
     *
     * @param file File to load properties from.
     */
    fun load(file: File): Properties {
        return load( Properties(), file)
    }

    /**
     * Save properties in a file.
     *
     * @param props Properties to save.
     * @param file File to save properties to.
     */
    fun save(props: Properties, file: File) {
        try {
            FileWriter(file).use { fw -> props.store(fw, SimpleDateFormat().format(Date())) }
        } catch(rte: RuntimeException) {
            Logz.errorMsg(file, rte)
        }
    }
}
