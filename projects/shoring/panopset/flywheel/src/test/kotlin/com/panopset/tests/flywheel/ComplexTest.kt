package com.panopset.tests.flywheel

import com.panopset.flywheel.FlywheelBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ComplexTest {
    @Test
    fun testVariableDefinition() {
        val script = FlywheelBuilder().construct()
        script.put(FOO, BAR)
        script.exec()
        Assertions.assertEquals(BAR, script.getEntry(FOO))
    }

    @Test
    fun testScript() {
        SimpleTest().comparisonTest(TEMPLATE, "outdir/complexOut.html", EXPECTED)
    }

    companion object {
        const val TEMPLATE = "complexTest.txt"
        const val OUTPUT = "outdir/complexOut.html"
        const val EXPECTED = "complexTestExpected.html"
        private const val FOO = "foo"
        private const val BAR = "bar"
    }
}
