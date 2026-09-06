package com.panopset.compat

class AniGrid(
    mapw: Int, maph: Int, gridWidth: Int,
    gridHeight: Int
) {
    private val h: Int
    private val ht: Int
    private val w: Int
    var multiplier = 1.0

    constructor(
        mapWidth: Int, mapHeight: Int, gridWidth: Int, gridHeight: Int,
        multiplier: Double
    ) : this(mapWidth, mapHeight, gridWidth, gridHeight) {
        this.multiplier = multiplier
    }

    init {
        w = mapw / gridWidth
        h = maph / gridHeight
        val wt = w + (w * .5 * 0).toInt()
        ht = wt * h / w
    }

    val dspWidth: Int
        get() {
            val rtn = multiplier * w
            return rtn.toInt()
        }
    val dspHeight: Int
        get() {
            val rtn = multiplier * ht
            return rtn.toInt()
        }

    fun getPaintDimensions(gridx: Int, gridy: Int, x: Int, y: Int): IntArray {
        val sx1 = gridx * w
        val sy1 = gridy * h
        return intArrayOf(sx1, sy1, w, h, x, y, dspWidth, dspHeight)
    }
}
