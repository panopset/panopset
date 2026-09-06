package com.panopset.flywheel

import com.panopset.compat.Fileop.copyInputStreamToFile
import com.panopset.compat.Streamop.getTextFromStream
import com.panopset.compat.Stringop.stringToList
import java.io.File
import java.io.InputStream
import java.io.StringWriter

object Rezop {
    fun loadFromRez(clazz: Class<*>, rezPath: String): String {
        val inputString = clazz.getResourceAsStream(rezPath) ?: return ""
        return getTextFromStream( inputString)
    }

    fun loadListFromTextResource(clazz: Class<*>, rezPath: String): List<String> {
        val inputStream = clazz.getResourceAsStream(rezPath) ?: return ArrayList()
        return textStreamToList( inputStream)
    }

    fun loadListFromTextResource(rezPath: String): List<String> {
        val inputStream = Rezop::class.java.getResourceAsStream(rezPath) ?: return ArrayList()
        return textStreamToList( inputStream)
    }

    private fun textStreamToList(inputStream: InputStream): List<String> {
        val lines = getTextFromStream( inputStream)
        return stringToList( lines)
    }

    private fun getResourceStream(clazz: Class<*>, resourcePath: String): InputStream? {
        return clazz.getResourceAsStream(resourcePath)
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
        val inputStream = getResourceStream(clazz, resourcePath) ?: return
        copyInputStreamToFile(inputStream, file)
    }

    fun copyTextResourceToFile(clazz: Class<*>, resourcePath: String, fileName: String) {
        val inputStream = getResourceStream(clazz, resourcePath) ?: return
        copyInputStreamToFile(inputStream, fileName)
    }

    fun pkg2resourcePath(clazz: Class<*>): String {
        val sw = StringWriter()
        sw.append("/")
        sw.append(clazz.getPackage().name.replace(".", "/"))
        sw.append("/")
        return sw.toString()
    }
}
