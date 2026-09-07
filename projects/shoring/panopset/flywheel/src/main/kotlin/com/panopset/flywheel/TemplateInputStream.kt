package com.panopset.flywheel

import com.panopset.compat.Logz
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class TemplateInputStream(inputStream: InputStream) :
    TemplateArray(inputStream2list( inputStream))

fun inputStream2list(inputStream: InputStream): List<String> {
    val rtn: MutableList<String> = ArrayList()
    try {
        BufferedReader(InputStreamReader(inputStream)).use { br ->
            var line = br.readLine()
            while (line != null) {
                rtn.add(line)
                line = br.readLine()
            }
        }
    } catch (ex: IOException) {
        Logz.errorEx(ex)
    }
    return rtn
}
