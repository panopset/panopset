package com.panopset.fxapp

import com.panopset.compat.Stringop

enum class FontSize(val value: Int, val imgRatio: Double, val svgRatio: Double) {
    SMALL(9, 1.0, 1.5),
    MEDIUM(12, 2.0, 2.0),
    LARGE(16, 3.0, 3.0),
    SUPER(24, 4.0, 4.0),
    CINEMA(48, 5.0, 5.0);

    companion object {
        fun findFromName(fontName: String): FontSize {
            if (!Stringop.isPopulated(fontName)) {
                return DEFAULT_SIZE
            }
            for (size in entries) {
                if (size.name == fontName) {
                    return size
                }
            }
            return DEFAULT_SIZE
        }

        fun findFromValue(fontSzAsStr: String): FontSize {
            if (!Stringop.isPopulated(fontSzAsStr)) {
                return DEFAULT_SIZE
            }
            for (size in entries) {
                if (String.format("%d", size.value) == fontSzAsStr) {
                    return size
                }
            }
            return DEFAULT_SIZE
        }

        fun find(fontSize: Int): FontSize {
            for (size in entries) {
                if (size.value == fontSize) {
                    return size
                }
            }
            for (size in entries) {
                if (size.value > fontSize) {
                    return size
                }
            }
            return DEFAULT_SIZE
        }

        val DEFAULT_SIZE = MEDIUM
    }
}
