package com.panopset.flywheel

data class LineFeedRules(var lineBreaks: Boolean, var listBreaks: Boolean) {
    override fun toString(): String {
        return "LineB:$lineBreaks ListB:$listBreaks"
    }
}

const val lineBreaksTrue = true
const val lineBreaksFalse = false
const val listBreaksTrue = true
const val listBreaksFalse = false

val LINE_BREAKS = LineFeedRules(lineBreaksTrue, listBreaksFalse)
val LIST_BREAKS = LineFeedRules(lineBreaksFalse, listBreaksTrue)
val FULL_BREAKS = LineFeedRules(lineBreaksTrue, listBreaksTrue)
val LFR_FLATTEN = LineFeedRules(lineBreaksFalse, listBreaksFalse)
