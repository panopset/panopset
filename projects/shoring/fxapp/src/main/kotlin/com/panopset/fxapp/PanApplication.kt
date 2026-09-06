package com.panopset.fxapp

import java.util.*

abstract class PanApplication(private val applicationInfo: ApplicationInfo) {

    fun getCompanyName(): String {
        return applicationInfo.getApplicationBranding().getCompanyName()
    }
    fun getApplicationDisplayName(): String {
        return applicationInfo.getApplicationDisplayName()
    }
    fun getDescription(): String {
        return applicationInfo.getDescription()
    }
    open fun afterShow(fxDoc: FxDoc) {

    }

    val applicationShortName = this.javaClass.name.lowercase(Locale.getDefault())
    val filesKey = applicationShortName + "_files"

    var id = 0L
    fun getNextUniqueID(): Long {
        return id++
    }
}
