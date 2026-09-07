package com.panopset.tests.flywheel

import org.junit.jupiter.api.Test

class VariableTest {
    @Test
    fun reuseVariableTest() {
        SimpleTest().comparisonTest(
            "variableReuseTest.txt", "variableReuseResult.txt",
            "variableReuseExpected.txt"
        )
    }
    @Test
    fun reuseVariable1Test() {
        SimpleTest().comparisonTest(
            "variableReuse1Test.txt", "variableReuse1Result.txt",
            "variableReuse1Expected.txt"
        )
    }
    @Test
    fun reuseVariable2Test() {
        SimpleTest().comparisonTest(
            "variableReuse2Test.txt", "variableReuse2Result.txt",
            "variableReuse2Expected.txt"
        )
    }
}
