package com.panopset.desk.utilities.dash

import java.io.File

object SshConfig {
    val configFile: File
    val map = HashMap<String, List<String>>()
    var mapLastModified: Long = 0

    init {
        val userHome = System.getProperty("user.home")
        val pathSeparator = File.separator
        configFile = File("$userHome${pathSeparator}.ssh${pathSeparator}config")
        refreshMap()
    }

    fun refreshMap() {
        mapLastModified = configFile.lastModified()
        val lines = configFile.readLines()
        var host = ""
        var hostName = ""
        var user = ""
        var identityFile = ""
        for (line in lines) {
            val fields = line.split(" ")
            if (fields.isNotEmpty() && fields.size > 1) {
                val key = fields[0].trim()
                val value = fields[1].trim()
                when (key) {
                    "Host" -> {
                        host = value
                    }
                    "HostName" -> {
                        hostName = value
                    }
                    "User" -> {
                        user = value
                    }
                    "IdentityFile" -> {
                        identityFile = value
                    }
                }
            }
            if (
                host.isNotEmpty() &&
                hostName.isNotEmpty() &&
                user.isNotEmpty() &&
                identityFile.isNotEmpty()
            ) {
                map[hostName] = arrayOf(host, hostName, user, identityFile).toList()
                host = ""
                hostName = ""
                user = ""
                identityFile = ""
            }
        }
    }
    fun getConfigForIP(ip: String): List<String> {
        if (mapLastModified != configFile.lastModified()) {
            refreshMap()
        }
        if (ip.isNotEmpty()) {
            val rtn = map[ip]
            if (rtn != null) {
                return rtn
            }
        }
        return emptyList()
    }
}

const val CONFIG_HOST_INDEX = 0
const val CONFIG_HOST_NAME_INDEX = 1
const val CONFIG_USER_INDEX = 2
const val CONFIG_IDENTITY_FILE_INDEX = 3