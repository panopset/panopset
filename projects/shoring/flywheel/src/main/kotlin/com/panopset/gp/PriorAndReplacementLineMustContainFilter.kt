package com.panopset.gp

import com.panopset.compat.ByLineFilter
import com.panopset.compat.FilteredString
import com.panopset.compat.Logz
import com.panopset.compat.Stringop

class PriorAndReplacementLineMustContainFilter(
    private val priorLineMustContain: String,
    private val replacementLineMustContain: String,
    private val searchList: ArrayList<String>,
    private val replaceList: ArrayList<String>
): ByLineFilter {
    constructor(priorLineMustContain: String, replacementLineMustContain: String, searchList: String, replaceList: String) :
            this( priorLineMustContain, replacementLineMustContain, Stringop.stringToList( searchList), Stringop.stringToList( replaceList))

    private var priorLine: String = ""
    override fun filter(str: String): FilteredString {
        var rtn = str
        if (!isValid( searchList, replaceList)) {
            return FilteredString(str)
        }
        if (Stringop.isPopulated(replacementLineMustContain)) {
            if (str.contains(replacementLineMustContain)) {
                return FilteredString(rtn)
            }
        }
        if (Stringop.isPopulated(priorLineMustContain)) {
            if (priorLine.contains(priorLineMustContain)) {
                rtn = doReplacements(searchList, replaceList, str)
            }
        } else {
            rtn = doReplacements(searchList, replaceList, str)
        }
        priorLine = str
        return FilteredString(rtn)
    }
}

private fun isValid(searchList: ArrayList<String>, replaceList: ArrayList<String>): Boolean {
    if (searchList.size != replaceList.size) {
        Logz.warn("Search (${searchList.size}) and replace list (${replaceList.size}) sizes do not match.")
        return false
    }
    if (searchList.size < 1) {
        Logz.warn("No search specified.")
        return false
    }
    return true
}
