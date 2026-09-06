package com.panopset.fxapp

object AnchorFactory {

    fun addAnchor(anchor: Anchor) {
        if (anchors.contains(anchor)) {
            return
        }
        anchors.add(anchor)
    }

    private val topAnchor: Anchor?
        get() = if (anchors.isEmpty()) {
            null
        } else anchors[0]

    private val copyOfAnchors: List<Anchor>
        get() {
            val rtn: MutableList<Anchor> = ArrayList()
            for (anchor in anchors) {
                rtn.add(anchor)
            }
            return rtn
        }

    private fun drop(anchor: Anchor) {
        if (anchors.contains(anchor)) {
            if (anchors.size == 1) {
                JavaFXapp.panExit()
            } else {
                anchors.remove(anchor)
            }
        }
    }

    private val anchors: MutableList<Anchor> = ArrayList()

    fun findAnchor(): Anchor? {
        return topAnchor
    }

    fun getAnchors(): List<Anchor> {
        return copyOfAnchors
    }

    fun removeAnchor(anchor: Anchor) {
        drop(anchor)
    }
}
