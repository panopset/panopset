package com.panopset.flywheel

import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import java.io.File
import java.io.StringWriter

/**
 * <h1>f - File</h1>
 *
 *
 *
 * Example 1
 *
 *
 * <pre>
 * ${&#064;f someFile.txt}
</pre> *
 *
 *
 *
 * Example 2
 *
 *
 * <pre>
 * ${&#064;p fileName}someFile.txt${&#064;q}
 * ${&#064;f &#064;fileame}
</pre> *
 *
 *
 * Output to the specified file, until the end of the template or another file command is found.
 *
 *
 */
class CommandFile(templateLine: TemplateLine, innerPiece: String, template: Template) :
    MatchableCommand( templateLine, innerPiece, template) {
    override fun resolve(sw: StringWriter) {
        if (template.flywheel.isOutputEnabled) {
            val newStringWriter = StringWriter()
            template.flywheel.writer = newStringWriter
            resolveMatchedCommands(newStringWriter)
            val outputFilePath = String.format("%s/%s",
                template.flywheel.targetDirectory, mapValueFirstThenExplicit(getParams()))
            Fileop.write( newStringWriter.toString(), File(outputFilePath))
            Logz.info("File command wrote to $outputFilePath.")
        } else {
            resolveMatchedCommands(sw)
        }
    }

    companion object {
        val shortHtmlText: String
            /**
             * Short HTML text for publishing command format in an HTML document.
             *
             * @return **${&#064;f somefile.txt}**.
             */
            get() = "\${&#064;f somefile.txt}"
    }
}
