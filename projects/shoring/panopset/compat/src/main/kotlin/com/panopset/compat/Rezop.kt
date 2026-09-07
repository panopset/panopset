package com.panopset.compat

import com.panopset.compat.Fileop.copyInputStreamToFile
import com.panopset.compat.Streamop.getTextFromStream
import com.panopset.compat.Stringop.stringToList
import java.io.File
import java.io.InputStream
import java.io.StringWriter

object Rezop {
    fun textStreamToList(inputStream: InputStream?): List<String> {
        if (inputStream == null) {
            return ArrayList()
        }
        val lines = getTextFromStream( inputStream)
        return stringToList( lines)
    }

    fun getPackageResourcePath(pkg: Package): String {
        val sw = StringWriter()
        sw.append("/")
        sw.append(pkg2path(pkg.name))
        return sw.toString()
    }

    fun pkg2path(dotName: String): String {
        return dotName.replace(".", "/")
    }

    fun copyTextResourceToFile(clazz: Class<*>, resourcePath: String, file: File) {
        val inputStream = clazz.getResourceAsStream(resourcePath)
        if (inputStream == null) {
            Logz.warn("$resourcePath not found.")
        } else {
            copyInputStreamToFile(inputStream, file)
        }
    }

    fun copyTextResourceToFile(clazz: Class<*>, resourcePath: String, fileName: String) {
        val inputStream = clazz.getResourceAsStream(resourcePath)
        if (inputStream == null) {
            Logz.warn("$resourcePath not found.")
        } else {
            copyInputStreamToFile(inputStream, fileName)
        }

    }

    fun pkg2resourcePath(clazz: Class<*>): String {
        val sw = StringWriter()
        sw.append("/")
        sw.append(clazz.getPackage().name.replace(".", "/"))
        sw.append("/")
        return sw.toString()
    }
}
