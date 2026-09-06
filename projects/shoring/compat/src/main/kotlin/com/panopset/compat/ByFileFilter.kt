package com.panopset.compat

import java.io.File

interface ByFileFilter {
    fun fileFilter(file: File): Boolean {
        return true
    }
}
