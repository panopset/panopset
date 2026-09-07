package com.panopset.flywheel

import java.io.StringWriter

abstract class Command(val template: Template, val templateLine: TemplateLine) {
    val id = UniqueIdFactory.getId()
    var next: Command? = null
    var prev: Command? = null

    abstract fun resolve(sw: StringWriter)
    fun resolveCommand(sw: StringWriter) {
        resolve(sw)
    }

    abstract fun getDescription(): String

    override fun toString(): String {
        val cn = this.javaClass.simpleName
        return "$id: ${cn.substring(7)} ${getDescription()}".replace("\n","")
    }
}
