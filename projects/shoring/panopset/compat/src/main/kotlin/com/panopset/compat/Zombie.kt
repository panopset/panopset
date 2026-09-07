package com.panopset.compat

object Zombie {
    var isActive = true
        private set

    fun stop() {
        if (!isActive) {
            return
        }
        isActive = false
        for (sa in stopActions) {
            sa.run()
        }
    }

    fun resume() {
        isActive = true
    }

    fun addStopAction(runnable: Runnable) {
        if (!stopActions.contains(runnable)) {
            stopActions.add(runnable)
        }
    }

    fun removeStopAction(runnable: Runnable) {
        stopActions.remove(runnable)
    }

    private val stopActions: MutableList<Runnable> = ArrayList()
}
