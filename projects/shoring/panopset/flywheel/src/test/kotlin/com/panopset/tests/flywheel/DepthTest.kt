package com.panopset.tests.flywheel

import org.junit.jupiter.api.Test

class DepthTest {
    @Test
    fun testDepth() {
        SimpleTest().comparisonTest(
            "depthTest.txt", "depthTest01.txt",
            "depthTest01Expected.txt"
        )
        SimpleTest().comparisonTest(
            "depthTest.txt", "depthTest02.txt",
            "depthTest02Expected.txt"
        )
    }

    @Test
    fun testDeeper() {
        SimpleTest().comparisonTest(
            "deepTest.txt", "deepTest.txt",
            "deepTestExpected.txt"
        )
    }
}
