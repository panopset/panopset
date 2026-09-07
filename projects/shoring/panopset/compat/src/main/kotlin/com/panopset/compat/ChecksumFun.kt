package com.panopset.compat

import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.security.MessageDigest

private fun summit(fis: FileInputStream, algorithm: String): String {
    val md = MessageDigest.getInstance(algorithm)
    return digestFromStream(md, fis)
}

private fun digestFromStream(md: MessageDigest, fis: FileInputStream): String {
    val dataBytes = ByteArray(1024)
    var nread: Int
    while (fis.read(dataBytes).also { nread = it } != -1) {
        md.update(dataBytes, 0, nread)
    }
    val mdbytes = md.digest()
    return convertBytesToHex(mdbytes)
}

fun convertBytesToHex(byts: ByteArray): String {
    val sb = StringBuilder()
    for (byt in byts) {
        sb.append(convertBytToHex(byt))
    }
    return sb.toString()
}

fun convertBytToHex(byt: Byte): String {
    return ((byt.toInt() and 0xff) + 0x100).toString(16).substring(1)
}

private fun getDigestOf(file: File, algorithm: String): String {
    return if (Files.isReadable(file.toPath())) {
        summit(FileInputStream(file), algorithm)
    } else{
        Nls.xlate("Can not read") + " " + Fileop.getCanonicalPath(file)
    }
}

fun byteCount(file: File): String {
    return String.format("%,d", file.length())
}

fun md5(file: File): String {
    return getDigestOf(file, "MD5")
}

fun sha1(file: File): String {
    return getDigestOf(file, "SHA-1")
}

fun sha256(file: File): String {
    return getDigestOf(file, "SHA-256")
}

fun sha384(file: File): String {
    return getDigestOf(file, "SHA-384")
}

fun sha512(file: File): String {
    return getDigestOf(file, "SHA-512")
}
