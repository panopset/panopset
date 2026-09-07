package com.panopset.compat

/**
 * See deploy.properties comments for version upgrade procedure.
 *
 * This class is generated from version.tmplt.
 */
object AppVersion {

    fun getVersion(): String {
        return "1.7.0"
    }

    fun getBuildNumber(): String {
        return "202512290711"
    }

    fun getFullVersion(): String {
        return getVersion() + "." + getBuildNumber()
    }

    @JvmStatic
    fun main(vararg args: String?) {
        if (args.isNotEmpty()) {
            Logz.yellow("AppVersion called with $args")
        }
        println(getVersion())
    }
}
