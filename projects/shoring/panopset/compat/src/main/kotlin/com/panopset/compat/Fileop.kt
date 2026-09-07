package com.panopset.compat

import com.panopset.compat.Streamop.copyChars
import com.panopset.compat.Streamop.getLinesFromReader
import com.panopset.compat.Streamop.streamToWriter
import com.panopset.compat.Stringop.FSP
import com.panopset.compat.Stringop.getEol
import com.panopset.compat.Stringop.isBlank
import java.io.*
import java.nio.file.Files
import java.util.*
import kotlin.collections.ArrayList

object Fileop {

    val userHome = File(System.getProperty("user.home"))

    fun touch(file: File) {
        if (prepFileForWriting( file) && !file.createNewFile()) {
            Logz.warn("Unable to create ${getCanonicalPath(file)}")
        }
    }

    fun getStandardPath(file: File): String {
        return getCanonicalPath(file).replace("\\", "/")
    }

    fun getParentDirectory(file: File): String {
        if (file.exists()) {
            val fullFile = File(getCanonicalPath(file))
            return getCanonicalPath(fullFile.parentFile)
        }
        return getCanonicalPath(file.parentFile)
    }

    fun fileExists(file: File?): Boolean {
        return if (file == null) {
            false
        } else {
            file.exists() && file.isFile && file.canRead()
        }
    }

    fun dirExists(dir: File): Boolean {
        return dir.exists() && dir.isDirectory && dir.canRead()
    }

    fun createTempFile(fileName: String): File {
        return File(combinePaths(Stringop.TEMP_DIR_PATH, fileName))
    }

    fun write(strs: Array<String>, file: File) {
        if (!prepFileForWriting( file)) {
            return
        }
        try {
            FileWriter(file).use { fw ->
                BufferedWriter(fw).use { bw ->
                    for (s in strs) {
                        bw.write(s)
                        bw.write(getEol())
                    }
                }
            }
        } catch (ex: IOException) {
            Logz.errorEx(ex)
        }
    }

    fun write(strs: List<String>, file: File) {
        if (!prepFileForWriting( file)) {
            return
        }
        try {
            FileWriter(file).use { fw ->
                BufferedWriter(fw).use { bw ->
                    for (s in strs) {
                        bw.write(s)
                        bw.write(getEol())
                    }
                }
            }
        } catch (ex: IOException) {
            Logz.errorEx(ex)
        }
    }

 fun write(str: String, file: File) {
        if (!prepFileForWriting( file)) {
            return
        }
        try {
            FileWriter(file).use { fw -> BufferedWriter(fw).use { bw -> bw.write(str) } }
        } catch (ex: IOException) {
            Logz.errorEx(ex)
        }
    }

    private fun prepFileForWriting(file: File): Boolean {
        if (isBlank(file.path)) {
            Logz.errorMsg("Can't write to blank file path.")
            return false
        }
        val parentFile = file.parentFile
        if (parentFile == null) {
            Logz.errorMsg("Unable to write to file with no parent directory.")
            return false
        }
        file.parentFile.mkdirs()
        return true
    }

 fun getCanonicalPath(file: File?): String {
        if (file == null) {
            return ""
        }
        return file.canonicalPath
    }

    fun isFileOneOfExtensions(file: File, extsToTry: String): Boolean {
            val fileExtension = getExtension(file)
            if (extsToTry.contains(",")) {
                val exts = extsToTry.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (ext in exts) {
                    if (ext.trim { it <= ' ' }.equals(fileExtension, ignoreCase = true)) {
                        return true
                    }
                }
                return false
            } else {
                return getExtension(file).equals(extsToTry, ignoreCase = true)
            }
    }

 fun getExtension(fileName: String): String {
        val i = fileName.lastIndexOf(46.toChar())
        return if (i > 0) fileName.substring(i + 1) else ""
    }

    fun getExtension(file: File): String {
        return getExtension(file.getName())
    }

    fun combinePaths(dirPath: String, path: String): String {
        return combinePaths(File(dirPath), path)
    }

    fun combinePaths(dir: File, path: String): String {
        return String.format("%s%s%s", getCanonicalPath(dir), FSP, path)
    }

    fun readTextFile(filePath: String): String {
        return readTextFile( File(filePath))
    }

    fun readTextFile(file: File): String {
        if (!file.exists()) {
            Logz.warn(String.format("File %s doesn't exist.", getCanonicalPath(file)))
            return ""
        }
        val sw = StringWriter()
        try {
            copyChars(FileReader(file), sw)
        } catch (ex: IOException) {
            Logz.errorMsg(file, ex)
        }
        return sw.toString()
    }

 fun readLines(file: File): List<String> {
        if (!file.exists()) {
            Logz.errorMsg("File does not exist", file)
            return ArrayList()
        }
        return try {
            getLinesFromReader(FileReader(file))
        } catch (ex: FileNotFoundException) {
            Logz.errorEx(ex)
            ArrayList()
        }
    }

    fun delete(file: File) {
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (f in files) {
                    delete( f)
                }
            }
        }
        if (file.exists()) {
            try {
                Files.delete(file.toPath())
            } catch (e: IOException) {
                Logz.errorEx(e)
            }
        }
    }

    fun mkdirs(path: File) {
        if (!path.mkdirs()) {
            Logz.errorMsg("${Nls.xlate("Unable to create path to")},${getCanonicalPath(path)}")
        }
    }

    fun copyInputStreamToFile(inp: InputStream, fileName: String) {
        copyInputStreamToFile(inp, File(fileName))
    }

    fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        streamToWriter(inputStream, FileWriter(file))
    }

    fun moveFile(ff: File, tf: File) {
        Files.move(ff.toPath(), tf.toPath())
    }

    fun copyFile(ff: File, tf: File) {
        try {
            Files.copy(ff.toPath(), tf.toPath())
        } catch (e: IOException) {
            Logz.errorEx(e)
        }
    }

    val TEMP_DIRECTORY = File(Stringop.USH + "/Documents/temp")

    fun removeExtension(str: String): String {
        val i = str.lastIndexOf(".")
        return if (i > -1) {
            str.substring(0, i)
        } else str
    }

 fun checkParent(targetFile: File): Boolean {
        val parent = targetFile.parentFile
        return if (parent.exists()) {
            true
        } else {
            parent.mkdirs()
        }
    }

    fun loadProps(fileName: String): Properties {
        return loadProps(File(fileName))
    }

    fun loadProps(file: File): Properties {
        val rtn = Properties()
        try {
            FileReader(file).use { fileReader ->
                BufferedReader(fileReader).use {
                    rtn.load(it)
                }
            }
        } catch (ex: Exception) {
            Logz.errorMsg(file, ex)
        }
        return rtn
    }
}

fun props2map(props: Properties): MutableMap<String, String> {
    val rtn = HashMap<String, String>()
    for (e in props.entries) {
        rtn[e.key.toString()] = e.value.toString()
    }
    return rtn
}
