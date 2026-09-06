package com.panopset.marin.bootstrap

import com.panopset.compat.AppVersion.getVersion
import com.panopset.compat.Stringop
import com.panopset.compat.Trinary
import com.panopset.compat.UrlHelper

object VersionHelper {

    private const val OFFLINE = "Offline"
    private var availVers = ""
    fun getAvailableVersion(): String {
        if (availVers.isEmpty()) {
            val panURL = "https://panopset.com"
            val versJson = UrlHelper.getTextFromURL( "$panURL/version.json", )
            availVers = if (Stringop.isBlank(versJson)) {
                OFFLINE
            } else {
                versJson.replace("\"".toRegex(), "").replace("\n".toRegex(), "").replace("\r".toRegex(), "")
            }

        }
        return availVers
    }

    fun isReadyToUpdate(): Trinary {
        val thisVersion = getVersion()
        val availVersion = getAvailableVersion()
        if (OFFLINE == availVersion) {
            return Trinary.ERROR
        }
        return if (availVersion == thisVersion) Trinary.FALSE else {
            if (availVersion.length == thisVersion.length) {
                var i = 0
                for (char in thisVersion) {
                    if (++i == thisVersion.length) {
                        val thisVersionLastNumber = Integer.parseInt("$char")
                        val availVersionLastChar = availVersion.substring(availVersion.length - 1, availVersion.length)
                        val availVersionLastNumber = Integer.parseInt(availVersionLastChar)
                        if (availVersionLastNumber + 1 == thisVersionLastNumber) {
                            return Trinary.IMPOSSIBLE
                        }
                    }
                }
            }
            Trinary.TRUE
        }
    }
}
