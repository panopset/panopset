package com.panopset.flywheel

import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import com.panopset.compat.Stringop.getEol
import java.io.StringWriter

class CommandRaw(templateLine: TemplateLine, innerPiece: String, template: Template) :
    TemplateDirectiveCommand( templateLine, innerPiece, template) {
    override fun resolve(sw: StringWriter) {
        val ts = template.templateSource
        Logz.info(
            String.format(
                "Template %s executing raw as is file %s at line %4d.",
                ts.name,
                getParams(),
                ts.line
            )
        )
        val sourceFile = SourceFile( template.flywheel, getParams())
        for (line in Fileop.readLines( sourceFile.file)) {
            sw.append(line)
            sw.append(getEol())
        }
    }
}
