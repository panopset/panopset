package com.panopset.flywheel

class TemplateLine(
    val line: String,
    val templateCharIndex: Int, val templateLineNumber: Int) {
    constructor(line: String) : this(line, 0, 0)
    override fun toString(): String {
        return String.format(" line#: %5d, char#: 5%d line: %s", templateLineNumber, templateCharIndex, line)
    }
}
