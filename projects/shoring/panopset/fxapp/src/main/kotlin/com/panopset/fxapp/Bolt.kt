package com.panopset.fxapp

interface Bolt {
    fun getBoltValue(): String
    fun getBoltDefault(): String
    fun setBoltValue(value: String)
}
