package com.panopset.compat

data class FilteredString(val str: String, val isDeleted: Boolean) {
    constructor(str: String) : this(str, false)
}
