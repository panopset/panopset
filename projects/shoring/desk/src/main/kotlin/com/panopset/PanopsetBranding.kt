package com.panopset

import com.panopset.compat.AppVersion
import com.panopset.compat.Trinary
import com.panopset.fxapp.ApplicationBranding
import com.panopset.fxapp.FxDoc
import com.panopset.marin.bootstrap.VersionHelper

class PanopsetBranding: ApplicationBranding {
    override fun getCompanyName(): String {
        return "Panopset"
    }

    override fun updateVersionMessage(fxDoc: FxDoc) {
        when (VersionHelper.isReadyToUpdate()) {
            Trinary.ERROR -> fxDoc.warn(
                "Unable to get version information from panopset.com, please check log for errors."
            )

            Trinary.FALSE -> {
                fxDoc.green("You are running the current version, ${AppVersion.getVersion()}.")
            }

            Trinary.IMPOSSIBLE -> {
                fxDoc.green("Thank you for previewing the next version, ${AppVersion.getVersion()}")
            }

            Trinary.TRUE -> fxDoc.green(
                "  You are running version ${AppVersion.getVersion()}.  " +
                        "The current version is ${VersionHelper.getAvailableVersion()}, now available on panopset.com.",
            )
        }
    }
}
