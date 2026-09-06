package com.panopset.flywheel

import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import java.io.File
import java.io.StringWriter

class CommandMap (
    templateLine: TemplateLine, innerPiece: String,
    template: Template
) : TemplateDirectiveCommand( templateLine, innerPiece, template) {
    override fun resolve(sw: StringWriter) {
        val mapFileName = mapValueFirstThenExplicit(getParams())
        val mapFile = File(mapFileName)
        if (!mapFile.exists()) {
            Logz.errorMsg("File not found: $mapFileName")
        }
        val props  = Fileop.loadProps( mapFile)
        for (key in props.propertyNames()) {
            template.flywheel.put(key.toString(), props.getProperty(key.toString()))
        }
        super.resolve(sw)
    }
}
