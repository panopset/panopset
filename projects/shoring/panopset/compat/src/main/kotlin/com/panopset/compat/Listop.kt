package com.panopset.compat

class Listop {
    fun filter(input: String, filter: String): String {
        val sb = StringBuffer()
        FOR@ for (inp in Stringop.stringToList( input)) {
            for (fs in Stringop.stringToList( filter)) {
                if (inp == fs) {
                    continue@FOR
                }
            }
            sb.append(inp)
            sb.append(Stringop.getEol())
        }
        return sb.toString()
    }
}
