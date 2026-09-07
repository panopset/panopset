package com.panopset.compat

import java.io.StringWriter
import java.net.NetworkInterface

object NetworkInfo {
    private val netInts = ArrayList<NetInt>()
    var maca = ""
    var netSummary = ""
    var netVerbose = ""
    init {
        for (ni in NetworkInterface.networkInterfaces()) {
            val netInt = if (ni.hardwareAddress == null) {
                NetInt("", ni.name, ni.isLoopback, ni.isPointToPoint)
            } else {
                NetInt(convertBytesToHumanReadableColon(ni.hardwareAddress), ni.name, ni.isLoopback, ni.isPointToPoint)
            }
            for (inetAddress in ni.inetAddresses) {
                if (inetAddress.address.size == 4) {
                    netInt.addrInfos.add(AddrInfo(convertBytesToHumanReadableIP4(inetAddress.address)))
                } else {
                    netInt.addrInfos.add(AddrInfo(convertBytesToHumanReadableColon4(inetAddress.address)))
                }
            }
            netInts.add(netInt)
        }
        createNetSummary(false)
    }

    override fun toString(): String {
        return createNetSummary(false)
    }

    fun createNetSummary(verbose: Boolean): String {
        if (verbose) {
            if (netVerbose.isNotEmpty()) {
                return netVerbose
            }
        } else {
            if (netSummary.isNotEmpty()) {
                return netSummary
            }
        }
        val sw = StringWriter()
        for (netInt in netInts) {
            if (netInt.maca.isNotEmpty()) {
                if (verbose) {
                    sw.append("Network Interface Card (NIC) Medium Access Control MAC Address, \n" +
                            ": Flywheel variable com.panopset.MACAddress: ${netInt.maca}\n")
                } else {
                    sw.append("Flywheel variable com.panopset.MACAddress: ${netInt.maca}\n")
                }
                this.maca = netInt.maca
            }
            sw.append(netInt.name)
            if (verbose) {
                sw.append(", loop back: ${netInt.isLoopBack}")
                sw.append(", point to point: ${netInt.isPointToPoint}")
            }
            for (addInfo in netInt.addrInfos) {
                sw.append("\n")
                sw.append(addInfo.ipAddress)
            }
            sw.append("\n\n")
        }
        if (verbose) {
            netVerbose = sw.toString()
        } else {
            netSummary = sw.toString()
        }
        return sw.toString()
    }
}

data class NetInt(val maca: String, val name: String, val isLoopBack: Boolean, val isPointToPoint: Boolean) {
    val addrInfos = ArrayList<AddrInfo>()
}

data class AddrInfo(val ipAddress: String)

fun convertBytesToHumanReadableColon(byts: ByteArray): String {
    val sb = StringBuilder()
    var firstTime = true
    for (byt in byts) {
        if (firstTime) {
            firstTime = false
        } else {
            sb.append(":")
        }
        sb.append(convertBytToHex(byt))
    }
    return sb.toString()
}

fun convertBytesToHumanReadableColon4(byts: ByteArray): String {
    val sb = StringBuilder()
    var byteCount = 0
    for (byt in byts) {
        if (byteCount++ > 1) {
            sb.append(":")
            byteCount = 1
        }
        sb.append(convertBytToHex(byt))
    }
    return sb.toString()
}
fun convertBytesToHumanReadableIP4(byts: ByteArray): String {
    val sb = StringBuilder()
    var firstTime = true
    for (byt in byts) {
        if (firstTime) {
            firstTime = false
        } else {
            sb.append(".")
        }
        val signedInt = byt.toInt()
        if (signedInt < 0) {
            val unSignedInt = signedInt + 256
            sb.append(unSignedInt)
        } else {
            sb.append(signedInt)
        }
    }

    return sb.toString()
}
