package com.panopset.marin.secure.checksums

import com.panopset.compat.*
import com.panopset.compat.Fileop.getCanonicalPath
import java.io.File

class ChecksumReport(private val textProcessor: TextProcessor) {
    private var firstTime = true
    fun generateReport(file: File, types: List<ChecksumType>) {
        textProcessor.clear()
        if (!file.exists()) {
            Logz.warn(
                java.lang.String.join(":", Nls.xlate("File doesn't exist"), getCanonicalPath(file))
            )
            return
        }
        if (file.isDirectory) {
            generateDirectoryReport(file, types)
        } else {
            generateFileReport(file, types)
        }
    }

    private var maxTitleLength: Int = 0
    private fun getMaxTitleSize(types: List<ChecksumType>): Int {
            var i = 0
            for (cst in types) {
                if (cst.name.length > i) {
                    i = cst.name.length
                }
            }
            maxTitleLength = i
        return maxTitleLength
    }

    private fun generateFileReport(
        file: File, types: List<ChecksumType>
    ) {
        Logz.green(java.lang.String.join(": ", Nls.xlate("Processing"), getCanonicalPath(file)))
        if (types.size > 1) {
            textProcessor.append(file.name)
            textProcessor.append("\n")
        } else if (types.size == 1) {
            if (firstTime) {
                textProcessor.append(types[0].name)
                textProcessor.append("\n")
                firstTime = false
            }
        }
        for (cst in types) {
            if (types.size > 1) {
                textProcessor.append(Padop.leftPad(cst.name, getMaxTitleSize(types), ' '))
                textProcessor.append(":")
            }
            when (cst) {
                ChecksumType.BYTES -> textProcessor.append(byteCount(file))
                ChecksumType.MD5 -> textProcessor.append(md5(file))
                ChecksumType.SHA1 -> textProcessor.append(sha1(file))
                ChecksumType.SHA256 -> textProcessor.append(sha256(file))
                ChecksumType.SHA384 -> textProcessor.append(sha384(file))
                ChecksumType.SHA512 -> textProcessor.append(sha512(file))
            }
            if (types.size > 1) {
                textProcessor.append("\n")
            } else {
                textProcessor.append(" ${file.name}\n")
            }
        }
        Logz.green(String.format("%s: %s", Nls.xlate("Completed"), getCanonicalPath(file)))
    }

    private fun generateDirectoryReport(
        file: File, types: List<ChecksumType>
    ) {
        var fount = false
        if (!file.isDirectory) {
            return
        }
        val fileList = file.listFiles() ?: return
        for (f in fileList) {
            if (f.isFile) {
                fount = true
                generateFileReport(f, types)
            }
        }
        if (fount) {
            Logz.green(Nls.xlate("Completed."))
        } else {
            Logz.warn(String.format(Stringop.CS, Nls.xlate("No files found in"), getCanonicalPath(file)))
        }
    }
}
