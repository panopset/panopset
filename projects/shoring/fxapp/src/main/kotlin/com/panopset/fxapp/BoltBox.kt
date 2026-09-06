package com.panopset.fxapp

class BoltBox(private val bolt: Bolt) {
    private var initialized = false

    fun getValue(): String {
        val currentValue = bolt.getBoltValue()
        if (initialized) {
            return currentValue
        } else {
            initialized = true
            if (currentValue.isEmpty()) {
                bolt.setBoltValue(bolt.getBoltDefault())
            }
        }
        return currentValue
    }

    fun setValue(value: String) {
        initialized = true
        bolt.setBoltValue(value)
    }
}
