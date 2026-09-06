package com.panopset.compat

import java.io.File

fun deleteLines(file: File, searchCriteria: String) {
    val targets = Stringop.stringToList( searchCriteria)
    if (targets.isEmpty()) {
        return
    }
    FileProcessor(file).withLineFilter(DeleteLinesLineFilter(targets)).exec()
}

class DeleteLinesLineFilter(private val targets: List<String>) : ByLineFilter {
    override fun filter(str: String): FilteredString {
        var filteredString = FilteredString(str)
        for (s in targets) {
            if (str.contains(s)) {
                filteredString = FilteredString(filteredString.str, true)
            }
        }
        return filteredString
    }
}
