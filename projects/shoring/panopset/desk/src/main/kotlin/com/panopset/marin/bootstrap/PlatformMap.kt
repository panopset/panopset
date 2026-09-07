package com.panopset.marin.bootstrap

import com.panopset.compat.AppVersion
import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import java.io.File
import java.util.*

class PlatformMap {
    val map: SortedMap<String, Platform> = Collections.synchronizedSortedMap(TreeMap())

    init {
        var launchDir = File(".")
        if (launchDir.canonicalPath.indexOf("desk") > -1) {
            launchDir = File("./../..")
        }
        for (file in launchDir.listFiles()!!) {
            if (file == null) {
                continue
            }
            if (file.extension == "properties") {
                if (file.name.length > 3) {
                    if (file.name.substring(0, 3) == "app") {
                        addPlatformProperties(file)
                    }
                }
            }
        }
        if (map.isEmpty()) {
            Logz.errorMsg("No platform properties found in", launchDir)
        }
    }

    private fun addPlatformProperties(file: File) {
        val props = Fileop.loadProps(file)
        val dspOrd = props.getProperty("DSPORD") ?: "4"
        val fxArch: String = props.getProperty("FX_ARCH") ?: ""
        if (fxArch.isEmpty()) {
            return
        }
        //val platformName: String = Stringop.trimSurroundingQuotes(props.getProperty("PLATFORM_NAME") ?: "")
        if (fxArch.isEmpty()) {
            return
        }
        val installerPrefix = props.getProperty("INSTALLER_PFX")
        val installerSuffix = props.getProperty("INSTALLER_SFX")
        val artifactName = "$installerPrefix${AppVersion.getVersion()}$installerSuffix"
        map[dspOrd] = Platform(fxArch, artifactName)
    }
}
