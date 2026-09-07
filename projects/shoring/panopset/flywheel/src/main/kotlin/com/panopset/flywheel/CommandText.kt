package com.panopset.flywheel

import com.panopset.compat.Logz
import java.io.BufferedReader
import java.io.IOException
import java.io.StringReader
import java.io.StringWriter

/**
 * Command to handle normal text entry.
 */
class CommandText(template: Template, templateLine: TemplateLine,
    private val text: String
):
    Command(template, templateLine) {
    override fun getDescription(): String {
        val sr = StringReader(text)
        try {
            BufferedReader(sr).use { br ->
                var str = br.readLine()
                return if (str == null) {
                    "".also { str = it }
                } else "CommandText:$str"
            }
        } catch (ex: IOException) {
            Logz.errorEx(ex)
            return ex.message!!
        }
    }

    override fun resolve(sw: StringWriter) {
        var rtn = text
        if (!template.flywheel.isReplacementsSuppressed) {
            for (s in template.flywheel.replacements) {
                rtn = rtn.replace(s[0], s[1])
            }
        }
        sw.append(rtn)
        Logz.debug(
            String.format(
                "%s %4d: %s", template.templateSource.name,
                template.templateSource.line, rtn
            )
        )
    }
}
