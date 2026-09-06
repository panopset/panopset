package com.panopset.compat

enum class AlertColor(val hexDkFg: String, val hexDkBg: String, val hexLtFg: String, val hexLtBg: String) {
    GRN(DK_FG_GREEN, DK_BG_GREEN, LT_FG_GREEN, LT_BG_GREEN),
    PPL(DK_FG_PURPLE, DK_BG_PURPLE, LT_FG_PURPLE, LT_BG_PURPLE),
    BLU(DK_FG_BLUE, DK_BG_BLUE, LT_FG_BLUE, LT_BG_BLUE),
    RED(DK_FG_RED, DK_BG_RED, LT_FG_RED, LT_BG_RED),
    ORG(DK_FG_ORANGE, DK_BG_ORANGE, LT_FG_ORANGE, LT_BG_ORANGE),
    YLW(DK_FG_YELLOW, DK_BG_YELLOW, LT_FG_YELLOW, LT_BG_YELLOW),
}

private val map = HashMap<Int, AlertColor>()

fun findAlertColor(index: Int): AlertColor {
    if (map.isEmpty()) {
        for (ac in AlertColor.entries) {
            map[ac.ordinal] = ac
        }
    }
    return map[index] ?: AlertColor.GRN
}

const val DK_FG_RED = "ff3333;"
const val DK_BG_RED = "000000;"
const val LT_FG_RED = "dd0000;"
const val LT_BG_RED = "ffffff;"

const val DK_FG_GREEN = "00ff00;"
const val DK_BG_GREEN = "000000;"
const val LT_FG_GREEN = "006600;"
const val LT_BG_GREEN = "ffffff;"

const val DK_FG_YELLOW = "ffff66;"
const val DK_BG_YELLOW = "000000;"
const val LT_FG_YELLOW = "ffff66;"
const val LT_BG_YELLOW = "ffffff;"

const val DK_FG_ORANGE = "ffaa00;"
const val DK_BG_ORANGE = "000000;"
const val LT_FG_ORANGE = "ffaa00;"
const val LT_BG_ORANGE = "ffffff;"

const val DK_FG_BLUE = "0000ff;"
const val DK_BG_BLUE = "000000;"
const val LT_FG_BLUE = "000099;"
const val LT_BG_BLUE = "ffffff;"

const val DK_FG_PURPLE = "ff3333;"
const val DK_BG_PURPLE = "000000;"
const val LT_FG_PURPLE = "ff3333;"
const val LT_BG_PURPLE = "ffffff;"
