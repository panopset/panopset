package com.panopset.gp

import com.panopset.compat.Logz
import java.io.File
import java.io.IOException

object FileCompare {
    fun filesAreSame(one: File, two: File): Boolean {
        try {
            val tfp1 = textFileIterator( one)
            val tfp2 = textFileIterator( two)
            while (tfp1.hasNext() && tfp2.hasNext()) {
                val str1 = tfp1.next()
                val str2 = tfp2.next()
                if (str1 != str2) {
                    Logz.warn(String.format("str1: %s\n         str2: %s", str1, str2))
                    return false
                }
            }
            if (tfp1.hasNext() || tfp2.hasNext()) {
                return false
            }
        } catch (ex: IOException) {
            Logz.errorEx(ex)
        }
        return true
    }
}
