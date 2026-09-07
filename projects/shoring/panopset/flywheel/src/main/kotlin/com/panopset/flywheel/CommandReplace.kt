package com.panopset.flywheel

import com.panopset.compat.Stringop
import java.io.StringWriter

/**
 * <h1>p - Replace</h1>
 *
 *
 *
 * In this example, &quot;Joe&quot; will be replaced with &quot;Mary&quot;.
 *
 *
 * <pre>
 *
 * ${&#064;p name}Joe${&#064;q}${&#064;r name}Mary${&#064;q}
 *
</pre> *
 *
 *
 *
 * In this example, a prior replacement is undone.
 *
 *
 * <pre>
 *
 * ${&#064;r name}${name}${&#064;q}
 *
</pre> *
 *
 */
class CommandReplace(
    templateLine: TemplateLine, innerPiece: String,
    template: Template
) : MatchableCommand( templateLine, innerPiece, template), UserMatchableCommand {
    override fun resolve(sw: StringWriter) {
        val tsw = StringWriter()
        resolveMatchedCommands(tsw)
        val toReplace = template.flywheel.getEntry(getParams())
        var key = getParams()
        if (Stringop.isPopulated(toReplace)) {
            key = toReplace
        }
        template.flywheel.replacements
            .add(arrayOf(key, tsw.toString()))
    }

    companion object {
        val shortHtmlText: String
            /**
             * Short HTML text for publishing command format in an HTML document.
             *
             * @return **${&#064;r name}**.
             */
            get() = "\${&#064;r name}"
    }
}
