package com.panopset.compat

interface ByLineFilter {
    fun filter(str: String): FilteredString
}
