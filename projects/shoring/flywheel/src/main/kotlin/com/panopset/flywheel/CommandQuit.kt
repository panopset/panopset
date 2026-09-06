package com.panopset.flywheel

import com.panopset.compat.Logz

/**
 * <h1>q - Quit</h1>
 *
 * <pre>
 * ${&#064;q}
</pre> *
 *
 *  * If the q command follows a p command, the String buffer is defined as the
 * variable name that was provided in the p command.
 *  * If the q command follows an l command, the String buffer is read for the
 * each item in the list.
 *
 *
 */
open class CommandQuit(
                       templateLine: TemplateLine, template: Template) : TemplateDirectiveCommand(
    templateLine, "", template
) {
    /**
     * Get matchable command.
     *
     * @return Matchable command that this Command Quit resolves.
     */
    var matchableCommand: MatchableCommand? = null
        private set

    /**
     * Match command.
     *
     * @param matchableCommand
     * Command to match this Command Quit with.
     */
    protected fun match(matchableCommand: MatchableCommand) {
        matchableCommand.commandQuit = this
        this.matchableCommand = matchableCommand
        Logz.info(String.format("%s successfully matched with Quit.", matchableCommand.getDescription()))
    }

    companion object {
        val shortHtmlText: String
            /**
             * Short HTML text for publishing command format in an HTML document.
             *
             * @return **${&#064;q}**.
             */
            get() = "\${&#064;q}"
    }
}
