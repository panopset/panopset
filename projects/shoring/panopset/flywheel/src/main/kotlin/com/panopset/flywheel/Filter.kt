package com.panopset.flywheel

import java.util.regex.Pattern

object Filter {
    fun findLine(value: String?): String {
        CommandExecute.flywheel!!.setFindLine(value!!)
        return ""
    }

    fun findLines(v0: String?, v1: String?): String {
        CommandExecute.flywheel!!.setFindLines(v0!!, v1!!)
        return ""
    }

    fun combine(value: String?): String {
        CommandExecute.flywheel!!.combine = value!!
        return ""
    }

    fun regex(regex: String, line: String?): String {
        resolveRegex(regex, line)
        return ""
    }

    private fun resolveRegex(regex: String, data: String?) {
        if (data == null) {
            CommandExecute.flywheel!!.stop("data is null")
            return
        }
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(data)
        var i = 0
        while (matcher.find()) {
            CommandExecute.flywheel!!.put(String.format("r%d", i++), matcher.group(1))
        }
    }
}
