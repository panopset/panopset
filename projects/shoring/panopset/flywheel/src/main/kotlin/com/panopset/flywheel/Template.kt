package com.panopset.flywheel

import com.panopset.compat.Logz
import java.io.StringWriter

/**
 * Once a Flywheel Script file is read, it becomes a template to be processed. Templates may also be
 * processed using the template directive within any template.
 */
class Template(val flywheel: Flywheel, val templateSource: TemplateSource, val templateRules: LineFeedRules) {
    constructor(flywheel: Flywheel, sourceFile: SourceFile, templateRules: LineFeedRules) :
            this(flywheel, TemplateFile(sourceFile.file), templateRules)

    private var sf: SourceFile? = null
    var commandFile: CommandFile? = null

    private val rawCommands: List<Command> = RawCommandLoader(this).load()
    private val topCommands: List<Command>

    init {
        addStructure(rawCommands)
        val commands = ImpliedQuitFilter().addImpliedQuits(rawCommands)
        println("************************************************************")
        println("============================================================")
        println("Template name: ${this.templateSource.name}")
        printTemplateStructure(commands)
        topCommands = CommandMatcher.matchQuitCommands(commands)
        println("****** Command Structure: **********************************")
        printTemplateStructure(topCommands)
        println("============================================================")
        println("************************************************************")
    }


    fun exec(sw: StringWriter) {
        val list = topCommands
        for (topCommand in list) {
            if (flywheel.isStopped) {
                return
            }
            try {
                topCommand.resolve(sw)
            } catch (e: FlywheelException) {
                Logz.errorEx(e)
                Logz.warn("Relative path: $relativePath")
                flywheel.stop(e.message?: "Flywheel Template exec failed.")
                return
            }
        }
    }

    fun output() {
        val list = topCommands
        try {
            for (topCommand in list) {
                topCommand.resolveCommand(flywheel.writer)
            }
        } catch (t: FlywheelException) {
            Logz.errorEx(t)
            stop(t.message?: "Flywheel Template output failed.")
        }
    }

    private fun stop(msg: String) {
        Logz.errorMsg(
            String.format(
                "Stopped while processing line %d: %s.",
                templateSource.line,
                templateSource.name
            )
        )
        flywheel.stop(msg)
    }

    val relativePath: String
        get() = sf?.relativePath ?: ""
}


fun printTemplateStructure(commands: List<Command>) {
    for (c in commands) {
        println(c.toString())
        if (c is MatchableCommand) {
            val mc: MatchableCommand = c
            for (d in mc.commands) {
                println(" $d")
            }
        }
    }
}
