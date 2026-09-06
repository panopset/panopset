package com.panopset.gp

import com.panopset.compat.Logz
import java.io.*
import java.nio.charset.StandardCharsets

class TextFileProcessor(reader: Reader) : LineIterator( reader)

fun textFileIterator(file: File): TextFileProcessor {
    return TextFileProcessor( InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8))
}
