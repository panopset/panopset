package com.panopset.tests.flywheel

import org.junit.jupiter.api.Test

class CommandRawTest {
    @Test
    fun testRawFile() {
        SimpleTest().comparisonTest("rawTest.txt", "rawTest.txt",
            "rawTestExpected.txt")
    }
    @Test
    fun testRawFileMultiLine() {
        SimpleTest().comparisonTest("rawTest2.txt", "rawTest2.txt",
            "rawTest2Expected.txt")
    }

}
