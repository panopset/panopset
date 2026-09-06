package com.panopset.desk.utilities.lowerclass

import com.panopset.compat.*
import java.io.*
import java.util.*
import java.util.jar.JarFile

internal class VersionMakeup() {
    private val map: MutableMap<String, Map<MajorVersion, Int>> = Collections.synchronizedSortedMap(TreeMap())
    private fun clear() {
        map.clear()
        classReport = StringWriter()
    }

    fun analyze(file: File, printDetails: Boolean) {
        clear()
        genRpt(file.getName(), file, printDetails)
    }

    val report: String
        get() {
            val tp = StringWriter()
            for ((key, jvs) in map) {
                tp.append(key)
                tp.append(" > ")
                var firstTime = true
                for ((key1, value) in jvs) {
                    if (firstTime) {
                        firstTime = false
                    } else {
                        tp.append(",")
                    }
                    tp.append(key1.strRep)
                    tp.append(" $value")
                }
                tp.append("\n")
            }
            if (Stringop.isPopulated(classReport.toString())) {
                tp.append("\n")
                tp.append("\n")
                tp.append(classReport.toString())
            }
            return tp.toString()
        }

    private fun genRpt(fileName: String, file: File?, printDetails: Boolean) {
        if (file == null) {
            Logz.warn("No file selected.")
            return
        }
        if (!file.exists()) {
            Logz.errorMsg("File doesn't exist.", file)
            return
        }
        if (file.isDirectory()) {
            genDirectoryReport(fileName, file, printDetails)
        } else {
            val ext = Fileop.getExtension(file.getName())
            if ("class" == ext) {
                genClassReport(fileName, file, printDetails)
            } else if ("jar" == ext) {
                genJarReport(file, printDetails)
            } else {
                Logz.warn("Selected file is not a jar or class.")
                return
            }
        }
        Logz.green(String.format("genReport complete for: %s", Fileop.getCanonicalPath(file)))
    }

    private fun genDirectoryReport(fileName: String, file: File, printDetails: Boolean) {
        val list = file.listFiles()
        if (list != null) {
            for (f in list) {
                if (f.isDirectory()) {
                    genDirectoryReport(fileName, f, printDetails)
                } else {
                    val ext = Fileop.getExtension(f.getName())
                    if ("class" == ext) {
                        genClassReport(fileName, f, printDetails)
                    } else if ("jar" == ext) {
                        genJarReport(f, printDetails)
                    }
                }
            }
        }
    }

    private fun genClassReport(fileName: String, file: File, printDetails: Boolean) {
        updateReportMap(fileName, updateStatsForClass(fileName, file, printDetails))
    }

    private fun genJarReport(file: File, printDetails: Boolean) {
        Logz.green(String.format("Processing jar: %s", file.getName()))
        updateReportMap(file.getName(), updateStatsForJar(file, printDetails))
    }

    private fun updateReportMap(name: String, jvs: Map<MajorVersion, Int>?) {
        if (!jvs.isNullOrEmpty()) {
            map[name] = jvs
        }
    }

    private fun updateStatsForJar(dirFile: File, printDetails: Boolean): Map<MajorVersion, Int> {
        val jvs = createVersionMap()
        try {
            JarFile(dirFile).use { jar ->
                val enumEntries = jar.entries()
                while (enumEntries.hasMoreElements()) {
                    val entry = enumEntries.nextElement()
                    val inputStream= jar.getInputStream(entry)
                    try {
                        DataInputStream(inputStream).use { dis ->
                            val cv = readClassVersion(entry.name, dis, printDetails)
                            val count = jvs[cv.majorVersion]
                            if (count == null) {
                                jvs[cv.majorVersion] = 1
                            } else {
                                jvs[cv.majorVersion] = count + 1
                            }
                        }
                    } catch (ex: IOException) {
                        throw RuntimeException(ex)
                    }
                }
            }
        } catch (ex: IOException) {
            Logz.errorMsg(Fileop.getCanonicalPath(dirFile))
        }
        return jvs
    }

    private var classReport = StringWriter()
    private fun updateStatsForClass(
        name: String, dirFile: File,
        printDetails: Boolean
    ): Map<MajorVersion, Int> {
        Logz.green(String.format("Processing class: %s", name))
        val jvs = createVersionMap()
        try {
            FileInputStream(dirFile).use { fis ->
                DataInputStream(fis).use { dis ->
                    val cv = readClassVersion(name, dis, printDetails)
                    jvs[cv.majorVersion] = 1
                }
            }
        } catch (ex: IOException) {
            Logz.errorEx(ex)
        }
        return jvs
    }

    @Throws(IOException::class)
    private fun readClassVersion(name: String, dis: DataInputStream, printDetails: Boolean): ClassVersion {
        if (dis.available() > 0) {
            if ("class" == Fileop.getExtension(name)) {
                val magic = dis.readInt()
                if (magic != -0x35014542) {
                    Logz.errorMsg(
                        name + " is not a java class! this should be 0xcafebabe:"
                                + Integer.toHexString(magic)
                    )
                } else {
                    val minor = dis.readUnsignedShort()
                    val major = dis.readUnsignedShort()
                    val classVersion = ClassVersion(
                        MajorVersion.findFromHexString(Integer.toHexString(major)),
                        Integer.toHexString(minor)
                    )
                    if (printDetails) {
                        classReport.append(name)
                        classReport.append(" major: ")
                        classReport.append(classVersion.majorVersion.strRep)
                        classReport.append(" minor: ")
                        classReport.append(classVersion.minorVersion)
                        classReport.append("\n")
                    }
                    return classVersion
                }
            }
        }
        return ClassVersion(MajorVersion.UNDEFINED, "")
    }

    private fun createVersionMap(): MutableMap<MajorVersion, Int> {
        return Collections.synchronizedSortedMap(TreeMap())
    }
}
