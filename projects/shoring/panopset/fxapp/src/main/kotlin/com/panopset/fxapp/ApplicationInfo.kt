package com.panopset.fxapp

interface ApplicationInfo {
    fun getApplicationBranding(): ApplicationBranding
    fun getApplicationDisplayName(): String
    fun getDescription(): String
    fun updateVersionMessage(fxDoc: FxDoc) {
        return getApplicationBranding().updateVersionMessage(fxDoc)
    }
}
