package com.panopset.flywheel

import com.panopset.compat.Stringop
import com.panopset.compat.fixedLengths
import java.io.StringWriter
import java.util.*

class FlywheelListDriver private constructor(
    private var inputList: MutableList<String>,
    val templateStr: String) {
    var splitz: String = ""
    var tokens: String = ""
    var lineFeedRules: LineFeedRules = FULL_BREAKS

    private fun getInputList(): List<String> {
        return inputList
    }

    fun setInputList(value: MutableList<String>) {
        inputList = value
    }

    private fun hasSplitz(): Boolean {
        return splitz.isNotEmpty()
    }

    @get:Synchronized
    val output: String
        get() = if (hasSplitz()) {
            val sw = StringWriter()
            for (s in getInputList()) {
                val chunks: Iterable<String> = fixedLengths( splitz).split(s)
                val chunky: MutableList<String> = ArrayList()
                for (chunk in chunks) {
                    chunky.add(chunk)
                }
                setInputList(chunky)
                sw.append(processInput())
                sw.append(Stringop.getEol())
            }
            sw.toString()
        } else {
            processInput()
        }

    private fun processInput(): String {
        val sw = StringWriter()
        for (s in getInputList()) {
            if (s.isBlank()) {
                continue
            }
            val flywheel = FlywheelBuilder().map(createInputMapFrom(s))
                .inputList(Stringop.stringToList( templateStr)).withLineFeedRules(lineFeedRules)
                .withWriter(sw).construct()
            flywheel.exec()
            if (flywheel.isStopped) {
                return String.format("Stopped: %s", flywheel.control.stopReason)
            }
            if (flywheel.getTemplate().templateRules.listBreaks) {
                sw.append(Stringop.getEol())
            }
        }
        return sw.toString()
    }

    private fun createInputMapFrom(inputLine: String): Map<String, String> {
        val rtn: MutableMap<String, String> = HashMap()
        rtn["l"] = inputLine
        if (Stringop.isPopulated(inputLine)) {
            var i = 0
            val st = StringTokenizer(inputLine, " ")
            while (st.hasMoreTokens()) {
                rtn[String.format("w%d", i++)] = st.nextToken()
            }
            TokenVariableFactory().addTokensToMap(rtn, inputLine, tokens)
        }
        return rtn
    }

    class Builder() {
        constructor(inputList: MutableList<String>, templateStr: String) : this() {
            fp = FlywheelListDriver( inputList, templateStr)
        }

        constructor(inputArray: Array<String?>, templateStr: String) : this() {
            fp = FlywheelListDriver( Arrays.asList(*inputArray), templateStr)
        }

        lateinit var fp: FlywheelListDriver
        fun build(): FlywheelListDriver {
            if (fp.inputList.isEmpty()) {
                fp.inputList.add("")
            }
            return fp
        }

        fun withLineFeedRules(lineFeedRules: LineFeedRules): Builder {
            fp.lineFeedRules = lineFeedRules
            return this
        }

        fun withSplitz(value: String): Builder {
            fp.splitz = value
            return this
        }

        fun withTokens(value: String): Builder {
            fp.tokens = value
            return this
        }
    }
}
