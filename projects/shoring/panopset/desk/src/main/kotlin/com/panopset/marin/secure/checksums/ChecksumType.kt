package com.panopset.marin.secure.checksums

import com.panopset.compat.Stringop.isEmpty

enum class ChecksumType(@JvmField val key: String) {
    BYTES("bytes"),
    MD5("md5"),
    SHA1("sha1"),
    SHA256("sha256"),
    SHA384(
        "sha384"
    ),
    SHA512("sha512");

    companion object {
        fun find(name: String?): ChecksumType? {
            if (isEmpty(name)) {
                return null
            }
            for (cst in entries) {
                if (cst.name.equals(name, ignoreCase = true)) {
                    return cst
                }
            }
            return null
        }
    }
}
