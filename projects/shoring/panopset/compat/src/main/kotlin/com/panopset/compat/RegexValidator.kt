package com.panopset.compat

import java.util.regex.Pattern

class RegexValidator(regex: String?) {
    private var p: Pattern = Pattern.compile(regex?: "")

    fun matches(value: String): Boolean {
        return p.matcher(value).find()
    }
}
