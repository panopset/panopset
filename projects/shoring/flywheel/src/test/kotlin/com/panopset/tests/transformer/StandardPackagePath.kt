package com.panopset.tests.transformer

import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import java.io.File

class StandardPackagePath(private val packageName: String) {
    var packagePath: String = String.format("src/test/resources/%s", packageName.replace('.', '/'))

    fun getRezStr(fileName: String): String {
        return Fileop.readTextFile( getFile(fileName))
    }

    fun getFile(fileName: String): File {
        return File(String.format("%s/%s", packagePath, fileName))
    }
}
