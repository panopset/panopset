package com.panopset.flywheel

interface TemplateSource {
    val isDone: Boolean
    fun nextRow(): String
    fun nextTemplateSourceLine(): String {
        var rtn = nextRow()
        var lastBackslashIsReal = false
        if (rtn.isNotEmpty()) {
            if (rtn.length > 1) {
                val last2 = rtn.substring(rtn.length - 2)
                if ("\\\\" == last2) {
                    lastBackslashIsReal = true
                }
            }
            rtn = rtn.replace("\\\\\\\\".toRegex(), "\\\\")
            if (!lastBackslashIsReal) {
                val lastChar = rtn.substring(rtn.length - 1)
                if ("\\" == lastChar && !isDone) {
                    val s = rtn.substring(0, rtn.length - 1)
                    return if (isDone) {
                        s
                    } else {
                        String.format("%s%s", s, nextTemplateSourceLine())
                    }
                }
            }
        }
        return rtn
    }

    fun reset()
    val line: Int
    val name: String?
    val raw: String?
}
