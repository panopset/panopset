package com.panopset.compat

object UrlHelper {
    fun getTextFromURL(urlStr: String): String {
        return doGetHttp( urlStr).text
    }
}
