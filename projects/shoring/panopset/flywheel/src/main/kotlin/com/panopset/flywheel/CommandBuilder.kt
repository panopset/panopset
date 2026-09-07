package com.panopset.flywheel

class CommandBuilder() {
    private lateinit var template: Template
    private lateinit var templateLine: TemplateLine
    private var closeDirectiveLoc = 0
    private var command: Command? = null
    fun template(commandTemplate: Template): CommandBuilder {
        template = commandTemplate
        return this
    }

    fun source(templateLine: TemplateLine, newCloseDirectiveLoc: Int): CommandBuilder {
        this.templateLine = templateLine
        closeDirectiveLoc = newCloseDirectiveLoc
        return this
    }

    fun command(newCommand: Command): CommandBuilder {
        command = newCommand
        return this
    }

    fun construct(): Command {
        val rtn: Command
        val line = templateLine.line
        val ln = templateLine.templateLineNumber
        val tci = templateLine.templateCharIndex
        val innerPiece = line.substring(
            Syntax.getOpenDirective()
                .length, closeDirectiveLoc
        )
        if (innerPiece.indexOf(Syntax.getDirective()) == 0
            && innerPiece.length > 1
        ) {
            val cmd = innerPiece.substring(1, 2)[0]
            rtn = when (cmd) {
                Commands.FILE.charCode -> CommandFile( TemplateLine(line, tci, ln), innerPiece, template)
                Commands.PUSH.charCode -> CommandPush( templateLine, innerPiece, template)
                Commands.REPLACE.charCode -> CommandReplace( templateLine, innerPiece, template)
                Commands.LIST.charCode -> CommandList( templateLine, innerPiece, template)
                Commands.QUIT.charCode -> CommandQuit( templateLine, template)
                Commands.TEMPLATE.charCode -> CommandTplt( templateLine, innerPiece, template)
                Commands.EXECUTE.charCode -> CommandExecute( templateLine, innerPiece, template)
                Commands.RAW.charCode -> CommandRaw( templateLine, innerPiece, template)
                Commands.MAP.charCode -> CommandMap( templateLine, innerPiece, template)
                else -> CommandUnkown( templateLine, innerPiece, template)
            }
        } else {
            return CommandVariable( templateLine, innerPiece, template)
        }
        return rtn
    }
}
