package com.panopset.compat

import com.panopset.compat.Stringop.getEol
import java.io.*

object Streamop {

    fun getTextFromStream(inpStr: InputStream?): String {
        if (inpStr == null) {
            return ""
        }
        try {
            BufferedInputStream(inpStr).use { bis ->
                val sw = StringWriter()
                var inp = bis.read()
                while (inp != -1) {
                    sw.append(inp.toChar())
                    inp = bis.read()
                }
                return sw.toString()
            }
        } catch (ex: IOException) {
            Logz.errorEx(ex)
            return ""
        }
    }

    fun copyChars(reader: Reader, writer: Writer) {
        var firstTime = true
        BufferedReader(reader).use { br ->
            BufferedWriter(writer).use { bw ->
                var line = br.readLine()
                while (line != null) {
                    if (firstTime) {
                        firstTime = false
                    } else {
                        bw.write(getEol())
                    }
                    bw.write(line)
                    line = br.readLine()
                }
            }
        }
    }

    fun copyStream(inputStream: InputStream, outputStream: OutputStream) {
        BufferedInputStream(inputStream).use { inpStr ->
            BufferedOutputStream(outputStream).use { os ->
                var byt: Int
                while (inpStr.read().also { byt = it } != -1) {
                    os.write(byt)
                }
            }
        }
    }

    fun streamToWriter(inpStr: InputStream, w: Writer) {
        BufferedInputStream(inpStr).use {
            BufferedWriter(w).use { bw ->
                var byt: Int
                while (inpStr.read().also { byt = it } != -1) {
                    bw.write(byt)
                }
            }
        }
    }

    fun getLinesFromReader(reader: Reader): ArrayList<String> {
        val rtn = ArrayList<String>()
        try {
            BufferedReader(reader).use { br ->
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

    fun getLinesFromReaderWithEol(reader: Reader): ArrayList<String> {
        val rtn = ArrayList<String>()
        try {
            BufferedReader(reader).use { br ->
                var line = br.readLine()
                while (line != null) {
                    rtn.add(String.format("%s%s", line, getEol()))
                    line = br.readLine()
                }
            }
        } catch (ex: IOException) {
            Logz.errorEx(ex)
        }
        return rtn
    }
}
