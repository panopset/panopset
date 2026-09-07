package com.panopset.compat

const val COPYRIGHT = "©"

fun combineDelim(delim: String, vararg args: String): String {
    var rtn = ""
    var firstTime = true
    for (arg in args) {
        if (firstTime) {
            firstTime = false
        } else {
            rtn += delim
        }
        rtn += arg
    }
    return rtn
}

fun parseDelimToDoubleArray(delim: String, data: String): ArrayList<Double> {
    val rtn: ArrayList<Double> = ArrayList()
    for (str in data.split(delim)) {
        rtn.add(java.lang.Double.valueOf(str))
    }
    return rtn
}
