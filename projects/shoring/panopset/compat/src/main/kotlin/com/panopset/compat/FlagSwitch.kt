package com.panopset.compat

class FlagSwitch {
    private var value = false
    fun pull(): Boolean {
        value = !value
        return value
    }

    fun reset() {
        value = false
    }
}
