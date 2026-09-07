package com.panopset.compat

class Cmd80 {
    fun centerHeader(msg: String) {
        val buffer = (74 - msg.length) / 2
        val b = StringBuilder()
        var i = 0
        while (i++ < buffer) {
            b.append(" ")
        }
        val s = StringBuilder("***${b}${msg}${b}")
        while (s.length < 77) {
            s.append(" ")
        }
        s.append("***")
        println(s)
    }

    fun line80(s: String) {
        val buffer = (75 - s.length)
        val b = StringBuilder()
        var i = 0
        while (i++ < buffer) {
            b.append(" ")
        }
        println("* $s $b *")
    }

    fun line80b() {
        println("*                                                                              *")
    }

    fun line80() {
        println("********************************************************************************")
    }

}
