package com.panopset.fxapp

interface ApplicationBranding {
    fun getCompanyName(): String

    /**
     * Override this, if you wish to implement version checking.
     * @see com.panopset.PanopsetBranding
     */
    fun updateVersionMessage(fxDoc: FxDoc) {

    }
}
