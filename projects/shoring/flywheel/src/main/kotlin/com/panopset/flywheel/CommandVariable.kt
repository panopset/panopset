package com.panopset.flywheel

import com.panopset.compat.Stringop
import java.io.StringWriter

/**
 * <h1>Variable</h1> There is no command associated with a variable, so you drop the **&#064;**
 * directive indicator, and then you specify a variable just as you would in any ant script or unix
 * shell. The variable must have been defined either in a map provided to the script through
 * Flywheel.Builder.mergeMap, or a Push command. Please do not define numbers as variable names, as
 * they are used in list processing.
 *
 * <pre>
 * ${variableName}
 *
 * Flywheel pre-defined variables:
 * com.panopset.flywheel.template
 *
</pre> *
 *
 */
class CommandVariable(
                      templateLine: TemplateLine, innerPiece: String, template: Template) : TemplateCommand(
    templateLine, innerPiece, template
) {
    init {
        parms = innerPiece
    }

    override fun resolve(sw: StringWriter) {
        val combineStr = template.flywheel.combine
        if (Stringop.isPopulated(combineStr) && "l" == getParams()) {
            resolveCombiner(sw, combineStr)
            return
        }
        val parms = getParams()
        if (Stringop.isPopulated(parms)) {
            if (parms == ReservedWords.TEMPLATE) {
                sw.append(template.relativePath)
            } else {
                var tmplt = template.flywheel.getEntry(getParams())
                if (!template.flywheel.isReplacementsSuppressed) {
                    for (s in template.flywheel.replacements) {
                        tmplt = tmplt.replace(s[0], s[1])
                    }
                }
                val findLines = template.flywheel.findLines
                if (findLines.isEmpty()) {
                    sw.append(tmplt)
                } else {
                    for (fl in findLines) {
                        if (Stringop.isPopulated(fl)) {
                            if (tmplt.indexOf(fl) > -1) {
                                sw.append(tmplt)
                                continue
                            }
                        }
                    }
                }
            }
        }
    }

    private fun resolveCombiner(sw: StringWriter, combineStr: String) {
        val combineLines = Stringop.parseInt( combineStr)
        if (combineLines > 0) {
            CombineLineManager.INSTANCE.combine(sw, combineLines, template.flywheel.getEntry(getParams()))
        }
    }

    private enum class CombineLineManager {
        INSTANCE;

        var assembledValue = StringWriter()
        var currentCombine = 0
        fun combine(sw: StringWriter, combineLines: Int, value: String?) {
            assembledValue.append(value)
            if (++currentCombine == combineLines) {
                sw.append(assembledValue.toString())
                assembledValue = StringWriter()
                currentCombine = 0
            }
        }
    }

    companion object {
        val shortHtmlText: String
            /**
             * Short HTML text for publishing command format in an HTML document.
             *
             * @return **${variableName}**.
             */
            get() = "\${variableName}"
    }
}
