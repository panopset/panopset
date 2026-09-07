package com.panopset.compat

import java.io.File
import java.util.*

class FileProcessor(private val targetFile: File, private val isRecursive: Boolean = true) {

    val transformer = Transformer()
    var byFileFilters: MutableList<ByFileFilter> = ArrayList()

    fun exec() {
        if (isRecursive) {
            recursiveExec( targetFile)
        } else {
            if (targetFile.isDirectory) {
                for (f in Objects.requireNonNull(targetFile.listFiles())) {
                    if (f.isFile) {
                        processFile( f)
                    }
                }
            } else {
                ReportFileTransformer(targetFile, transformer).exec()
            }
        }
    }

    private fun recursiveExec(rFile: File) {
        if (rFile.isDirectory) {
            for (f in Objects.requireNonNull(rFile.listFiles())) {
                recursiveExec( f)
            }
        } else {
            processFile( rFile)
        }
    }

    private fun processFile(rFile: File) {
        for (byFileFilter in byFileFilters) {
            if (!byFileFilter.fileFilter(rFile)) {
                return
            }
        }
        ReportFileTransformer(rFile, transformer).exec()
    }


    fun withFileFilter(byFileFilter: ByFileFilter): FileProcessor {
        byFileFilters.add(byFileFilter)
        return this
    }

    fun withLineFilter(byLineFilter: ByLineFilter): FileProcessor {
        transformer.byLineFilters.add(byLineFilter)
        return this
    }
}
