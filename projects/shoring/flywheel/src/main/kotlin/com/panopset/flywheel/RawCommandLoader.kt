package com.panopset.flywheel

import com.panopset.compat.Logz
import com.panopset.compat.Stringop
import java.util.*

internal class RawCommandLoader(val template: Template) {
    private val tmplt: Template = template
    private val queue: Deque<TemplateLine> = ArrayDeque()
    private val commands: MutableList<Command> = ArrayList()

    fun load(): List<Command> {
        tmplt.templateSource.reset()
        var templateIndex = 0
        var lineNumber = 0
        var firstTime = true
        while (!tmplt.templateSource.isDone) {
            if (tmplt.flywheel.isStopped) {
                return ArrayList()
            }
            flushQueue()
            if (tmplt.flywheel.isStopped) {
                return ArrayList()
            }
            var line = tmplt.templateSource.nextTemplateSourceLine()
            if (firstTime) {
                firstTime = false
            } else {
                if (tmplt.templateRules.lineBreaks) {
                    line = String.format("%s%s", Stringop.getEol(), line)
                }
            }
            process(TemplateLine(line, templateIndex, lineNumber++))
            templateIndex += line.length
        }
        flushQueue()
        return commands
    }

    private fun flushQueue() {
        while (!queue.isEmpty()) {
            process(queue.pop())
            if (tmplt.flywheel.isStopped) {
                return
            }
        }
    }

    private fun loadCommand(command: Command) {
        commands.add(command)
        Logz.debug("Loading command: $command")
    }

    private fun process(templateLine: TemplateLine) {
        val line = templateLine.line
        val openDirectiveLoc = line.indexOf(Syntax.getOpenDirective())
        val closeDirectiveLoc = line.indexOf(Syntax.getCloseDirective())
        if (closeDirectiveLoc == -1 || openDirectiveLoc == -1) {
            loadCommand(CommandText( tmplt, templateLine, line))
            return
        }
        if (closeDirectiveLoc < openDirectiveLoc) {
            skipTo(templateLine, openDirectiveLoc)
            return
        }
        val endOfDirective = closeDirectiveLoc + Syntax.getCloseDirective().length
        if (openDirectiveLoc == 0) {
            val command = CommandBuilder().template(tmplt).source(templateLine, closeDirectiveLoc).construct()
            if (command is CommandUnkown) {
                skipTo(templateLine, 1)
            } else {
                val remainder = line.substring(endOfDirective)
                loadCommand(command)
                if (remainder.isNotEmpty()) {
                    queue.push(
                        TemplateLine(
                            remainder,
                            templateLine.templateCharIndex + openDirectiveLoc,
                            templateLine.templateLineNumber
                        )
                    )
                }
            }
        } else {
            skipTo(templateLine, openDirectiveLoc)
        }
    }

    private fun skipTo(templateLine: TemplateLine, pos: Int) {
        val line = templateLine.line
        val templateCharIndex = templateLine.templateCharIndex
        val templateLineNumber = templateLine.templateLineNumber
        val skippedTextLine = line.substring(0, pos)
        loadCommand(
            CommandText( tmplt,
                TemplateLine(skippedTextLine, templateCharIndex, templateLineNumber),
                skippedTextLine
            )
        )
        queue.push(TemplateLine(line.substring(pos), templateCharIndex + pos, templateLineNumber))
    }
}

fun addStructure(commands: List<Command>) {
    var prev: Command? = null
    for (command in commands) {
        command.prev = prev
        if (prev != null) {
            prev.next = command
        }
        prev = command
    }
}
