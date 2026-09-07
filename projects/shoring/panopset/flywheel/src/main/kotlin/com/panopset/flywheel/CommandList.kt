package com.panopset.flywheel

import com.panopset.compat.Logz
import com.panopset.compat.Rezop
import com.panopset.compat.Stringop
import java.io.StringWriter
import java.util.*

/**
 * <h1>l - List</h1>
 *
 *
 *
 * Example 1
 *
 *
 * <pre>
 * ${&#064;l someListFile.txt}
</pre> *
 *
 *
 *
 * Example 2
 *
 *
 * <pre>
 * ${&#064;p listFileName}someListFile.txt${&#064;q}
 * ${&#064;l @listFileName}
</pre> *
 *
 *
 * Read the given file, and for each line execute the template from this list
 * command up until its matching q command.
 *
 *
 *
 * You may also specify a jar resource, for example
 *
 *
 * <pre>
 * /com/company/myList.txt
</pre> *
 *
 *
 *
 * If no set of tokens is defined, each line will be stored in variable
 * **1**. If there are tokens defined, the line will be split by your tokens,
 * and stored in variables named after integers, in order. Tokens may be defined
 * by the Flywheel.Builder.tokens method, or by specifying them in the reserved
 * variable name **${com.panopset.flywheel.tokens}**
 *
 *
 *
 * Another way to split the lines is by columns. If no tokens are defined, you
 * may specify comma delimited column separators in the reserved variable name
 * **${com.panopset.flywheel.splits}**. For example **5,10**.
 *
 *
 *
 *
 * List commands can be nested, as each list command has its own cascading
 * variable definitions. Just remember that any top level list has to define
 * numbered variables as different variable names in order for lower level lists
 * to use them.
 *
 */
class CommandList(
    templateLine: TemplateLine, innerPiece: String,
    template: Template
) : MatchableCommand( templateLine, innerPiece, template), UserMatchableCommand {
    override fun resolve(sw: StringWriter) {
        val sourceFile = SourceFile(
            template.flywheel,
            getParams()
        )
        var lines: List<String>? = ArrayList()
        if (sourceFile.isValid) {
            lines = sourceFile.getTheSourceLines()
        } else {
            val inputStream = this.javaClass.getResourceAsStream(getParams())
            if (inputStream != null) {
                lines = Rezop.textStreamToList( inputStream)
            }
        }
        if (lines == null || lines.isEmpty()) {
            return
        }
        val tokens = template.flywheel.getEntry(ReservedWords.TOKENS)
        val splits = template.flywheel.getEntry(ReservedWords.SPLITS)

        template.flywheel.mapHold.pushNewMapStack("key_${Stringop.getNextJvmUniqueID()}_${getParams()}")

        for (s in lines) {
            if (!Stringop.isPopulated(s)) {
                continue
            }
            Logz.debug(String.format("CommandList line:%s", s))
            TokenVariableFactory().addTokensToMap(template.flywheel.mapHold.mapStack.peek().map, s, tokens, template)
            if (Stringop.isPopulated(splits)) {
                val st = StringTokenizer(splits, ",")
                var count = 1
                var startAt = 0
                var split = 0
                while (st.hasMoreElements()) {
                    split = st.nextToken().toInt() - 1
                    if (s.length >= split) {
                        template.flywheel.put(
                            "" + count++,
                            processValue(s.substring(startAt, split))
                        )
                        startAt = split
                    }
                }
                if (s.length > split) {
                    template.flywheel.put(
                        String.format("%d", count),
                        processValue(s.substring(split))
                    )
                }
            } else {
                template.flywheel.put("1", processValue(s))
            }
            resolveMatchedCommands(sw)
        }
        template.flywheel.mapHold.mapStack.pop()
    }

    /**
     * Process a list value, resolving any template commands within the line.
     *
     * @param str
     * String to be processed.
     * @return processed value.
     */
    private fun processValue(str: String): String {
        val sw = StringWriter()
        Template(template.flywheel, TemplateArray(arrayOf(str)), LFR_FLATTEN).exec(sw)
        return sw.toString()
    }

    companion object {
        val shortHtmlText: String
            /**
             * Short HTML text for publishing command format in an HTML document.
             *
             * @return **${&#064;l someListFile.txt}**.
             */
            get() = "\${&#064;l someListFile.txt}"
    }
}
