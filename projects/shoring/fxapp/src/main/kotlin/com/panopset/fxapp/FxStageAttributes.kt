package com.panopset.fxapp

import com.panopset.compat.PersistentMapFile
import com.panopset.compat.Stringop
import com.panopset.compat.combineDelim
import com.panopset.compat.parseDelimToDoubleArray
import javafx.geometry.Point2D
import javafx.stage.Screen
import javafx.stage.Stage

fun saveStageAttributes(key: FxDocPropKeys, stage: Stage, pmf: PersistentMapFile) {
    val x = stage.x.toString()
    val y = stage.y.toString()
    val w = stage.width.toString()
    val h = stage.height.toString()
    pmf.put(key.name, combineDelim("|", x, y, w, h))
}

fun restoreStageAttributes(
    key: FxDocPropKeys, stage: Stage, pmf: PersistentMapFile, defaultWidth: Int, defaultHeight: Int) {
    var isNew = true
    val dims = arrayOfNulls<Double>(4)
    val windims: String = pmf.getMapValue(key.name)
    if (Stringop.isPopulated(windims)) {
        isNew = false
        var i = 0
        for (x in parseDelimToDoubleArray("|", windims)) {
            dims[i++] = x
        }
    }
    val xloc = notNull(dims[0], 20.0)
    val yloc = notNull(dims[1], 20.0)
    val width = notNull(dims[2], 100.0)
    val height = notNull(dims[3], 100.0)
    val loc = Point2D(xloc, yloc)
    val center = Point2D(xloc + (width / 2), yloc + (height / 2))
    val w = notNull(dims[2], java.lang.Double.valueOf(defaultWidth.toDouble()))
    val h = notNull(dims[3], java.lang.Double.valueOf(defaultHeight.toDouble()))
    stage.width = w
    stage.height = h
    var stillVisible = false
    for (screen in Screen.getScreens()) {
        if (screen.bounds.contains(loc) || (screen.bounds.contains(center))) {
            stillVisible = true
            break
        }
    }
    if (!stillVisible || isNew) {
        stage.centerOnScreen()
    } else {
        stage.x = xloc
        stage.y = yloc
    }
}

fun notNull(d: Double?, dft: Double): Double {
    if (d == null) {
        return dft
    }
    return d
}
