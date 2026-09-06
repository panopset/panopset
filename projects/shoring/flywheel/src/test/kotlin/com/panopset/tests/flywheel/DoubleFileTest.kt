package com.panopset.tests.flywheel

import org.junit.jupiter.api.Test

class DoubleFileTest {
    /**
     * Test generating more than one files using Command File.
     */
    @Test
    fun testDoubleFileGeneration() {
        SimpleTest()
            .comparisonTest(
                "doubleFileTest.txt", arrayOf(
                    "dft0.txt",
                    "dft1.txt"
                ), arrayOf(
                    "dft_expected0.txt",
                    "dft_expected1.txt"
                )
            )
    }
}
