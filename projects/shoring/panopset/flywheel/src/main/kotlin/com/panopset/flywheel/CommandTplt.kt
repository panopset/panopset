package com.panopset.flywheel

import com.panopset.compat.Logz
import java.io.StringWriter

/**
 * <h1>t - Template</h1>
 *
 *
 *
 * Example 1
 *
 *
 * <pre>
 * ${&#064;t someTemplateFile.txt}
</pre> *
 *
 *
 *
 * Example 2
 *
 *
 * <pre>
 * ${&#064;p templateName}someTemplateFile.txt${&#064;q}
 * ${&#064;t &#064;templateName}
</pre> *
 *
 *
 *
 * Continue execution using the supplied template file.
 *
 */
class CommandTplt(
    templateLine: TemplateLine, innerPiece: String,
    template: Template
) : TemplateDirectiveCommand( templateLine, innerPiece, template) {
    /**
     * Get last command.
     *
     * @return Last command.
     */
    /**
     * Set last command.
     *
     * @param command
     * Last command.
     */
    /**
     * Last command.
     */
    var lastCommand: Command? = null
    override fun resolve(sw: StringWriter) {
        val ts = template.templateSource
        val templateFileName = mapValueFirstThenExplicit(getParams())
        Logz.info(
            String.format(
                "Template %s executing template %s at line %4d.",
                ts.name,
                templateFileName,
                ts.line
            )
        )
        val template = Template(
            template.flywheel, SourceFile( template.flywheel, templateFileName), template.templateRules
        )



        template.exec(sw)
    }

    companion object {
        val shortHtmlText: String
            /**
             * Short HTML text for publishing command format in an HTML document.
             *
             * @return **${&#064;t someTemplateFile.txt}**.
             */
            get() = "\${&#064;t someTemplateFile.txt}"
    }
}
