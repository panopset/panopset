package com.panopset.flywheel

object UniqueIdFactory {
    var i: Int = 1000
    fun getId(): Int {
        return i++
    }
}
