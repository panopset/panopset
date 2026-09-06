package com.panopset.flywheel

import java.io.StringWriter

/**
 * Any command that is after burner processed with the QuitCommand must extend
 * this class.
 */
abstract class MatchableCommand internal constructor(
    templateLine: TemplateLine, innerPiece: String,
    template: Template
) : TemplateDirectiveCommand(
    templateLine, innerPiece, template
) {
    /**
     * @return List of commands.
     */
    /**
     * Commands.
     */
    val commands: ArrayList<Command> = ArrayList()
    /**
     * @return Command quit for this matchable command.
     */
    /**
     * Set command quit for this matchable command.
     *
     * @param newCommandQuit
     * New command quit to set.
     */
    /**
     * Command quit.
     */
    var commandQuit: CommandQuit? = null

    /**
     * Resolve matched commands.
     *
     * @param sw
     * StringWriter.
     */
    fun resolveMatchedCommands(sw: StringWriter?) {
        for (command in commands) {
            if (template.flywheel.isStopped) {
                return
            }
            command.resolveCommand(sw!!)
        }
    }
}
