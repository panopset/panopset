package com.panopset.compat

data class HttpResponsePackage(val responseCode: Int, val text: String, val errorMessage: String) {
    constructor(responseCode: Int, text: String) : this(responseCode, text, "")
}
