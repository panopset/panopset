package com.panopset.desk.utilities

import com.panopset.compat.AppVersion

class Version {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println(AppVersion.getFullVersion())
        }
    }
}
