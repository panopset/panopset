package com.panopset.flywheel

import com.panopset.compat.Stringop
import java.io.StringWriter
import java.util.*

class TokenVariableFactory {
    fun addTokensToMap(map: MutableMap<String, String>, line: String, tokens: String) {
        if (Stringop.isPopulated(tokens)) {
            var i = 0
            val st = StringTokenizer(line, tokens)
            while (st.hasMoreTokens()) {
                map[String.format("t%d", i++)] = st.nextToken()
            }
        }
    }

    fun addTokensToMap(map: MutableMap<String, String>, line: String, tokens: String, template: Template) {
        if (Stringop.isPopulated(tokens)) {
            var i = 0
            val st = StringTokenizer(line, tokens)
            while (st.hasMoreTokens()) {
                map[String.format("t%d", i++)] = processValue(template, st.nextToken())
            }
        }
    }

    /**
     * TODO: Duplicate in CommandList
     * Process a list value, resolving any template commands within the line.
     *
     * @param str
     * String to be processed.
     * @return processed value.
     */
    private fun processValue(template: Template, str: String): String {
        val sw = StringWriter()
        Template(template.flywheel, TemplateArray(arrayOf(str)), LFR_FLATTEN).exec(sw)
        return sw.toString()
    }
}
