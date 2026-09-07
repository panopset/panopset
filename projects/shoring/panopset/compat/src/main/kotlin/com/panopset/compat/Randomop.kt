package com.panopset.compat

import java.io.StringWriter
import java.security.SecureRandom
import kotlin.math.abs

private val secureRandom = SecureRandom()

fun random(min: Int, max: Int): Int {
    return if (min == max) {
        min
    } else {
        var shift = min
        if (min > max) {
            shift = max
        }
        secureRandom.nextInt((abs((max - min).toDouble()) + 1).toInt()) + shift
    }
}

fun nextBytes(bytes: ByteArray?) {
    secureRandom.nextBytes(bytes)
}

fun nextInt(): Int {
    return secureRandom.nextInt()
}

fun nextLong(): Long {
    return secureRandom.nextLong()
}

fun getRandomHexString(length: Int): String {
    val sw = StringWriter()
    for (i in 0 until length) {
        sw.append(String.format("%s", random(0, 15)))
    }
    return sw.toString()
}
