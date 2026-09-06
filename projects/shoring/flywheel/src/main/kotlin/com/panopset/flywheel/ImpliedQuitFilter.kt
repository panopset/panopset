package com.panopset.flywheel

/**
 * Implied quit filter.
 *
 *
 * Adds implied Command Quits to the commands vector.
 *
 */
internal class ImpliedQuitFilter {
    /**
     * Add implied quits.
     *
     * @param commands
     * Commands to add implied quits to.
     * @return List of commands with implied Command Quits added.
     */
    fun addImpliedQuits(commands: List<Command>): List<Command> {
        val rtn: MutableList<Command> = ArrayList()
        var first: Command? = null
        var checkOnce = false
        for (command in commands) {
            if (first == null) {
                first = command
            }
            if (command is CommandFile) {
                insertImpliedQuitBefore(command)
                command.template.commandFile = command
            }
            if (command.next == null
                && command.template.commandFile != null
            ) {
                if (checkOnce) {
                    throw FlywheelException("Structure error.")
                }
                val iq = ImpliedQuit(command.template)
                command.next = iq
                iq.prev = command
                checkOnce = true
            }
        }
        var command = first
        while (command != null) {
            rtn.add(command)
            command = command.next
        }
        return rtn
    }

    /**
     * Insert implied quit.
     *
     * @param command
     * Command to insert before.
     */
    private fun insertImpliedQuitBefore(command: Command) {
        if (command.template.commandFile == null) {
            return
        }
        val iq = ImpliedQuit(command.template)
        iq.next = command
        if (command.prev != null) {
            iq.prev = command.prev
            command.prev!!.next = iq
        }
    }
}
