package com.panopset.flywheel

class Driver {
    fun doSayHello(): String {
        val clazzj = Class.forName("com.panopset.flywheel.Passenger")
        val clazzk = clazzj.kotlin
        val instx = clazzk.objectInstance
        val resz = clazzk.members.firstOrNull { it.name == "sayHello" }?.call(instx)
        println(resz)
        return resz.toString()
    }
}
