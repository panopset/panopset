package com.panopset.gp

import com.panopset.compat.*
import java.io.File

class GlobalReplaceProcessor(
    private val file: File,
    private val extensionsList: String,
    private val recursive: Boolean
) {

    fun process(byLineFilter: ByLineFilter) {
        val byFileFilter: ByFileFilter = object: ByFileFilter {
            override fun fileFilter(file: File): Boolean {
                if (file.isDirectory) {
                    Logz.warn("Ignoring directory: ${Fileop.getCanonicalPath(file)}")
                    return false
                }
                Logz.debug("Processing ${file.canonicalPath}")
                return !(Stringop.isPopulated(extensionsList) && !Fileop.isFileOneOfExtensions(file, extensionsList))
            }
        }
        FileProcessor(file, recursive).withFileFilter(byFileFilter).withLineFilter(byLineFilter).exec()
        Logz.green("Done")
    }
}

fun doReplacements(searchStrs: List<String>, replacementStrs: List<String>, fromStr: String): String {
    var rtn = fromStr
    for (i in searchStrs.indices) {
        rtn = doReplacement(searchStrs[i], replacementStrs[i], rtn)
    }
    return rtn
}

private fun doReplacement(searchStr: String, replacementStr: String, fromStr: String): String {
    return fromStr.replace(searchStr, replacementStr)
}
