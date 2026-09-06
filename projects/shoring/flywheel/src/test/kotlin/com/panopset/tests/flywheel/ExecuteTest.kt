package com.panopset.tests.flywheel

import org.junit.jupiter.api.Test

class ExecuteTest {
    @Test
    fun testExecuteCommand() {
        SimpleTest().comparisonTest(
            "executeTest.txt", "executeTest.txt",
            "executeTestExpected.txt"
        )
    }
}
