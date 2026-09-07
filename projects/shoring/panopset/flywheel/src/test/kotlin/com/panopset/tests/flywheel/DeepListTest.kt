package com.panopset.tests.flywheel

import org.junit.jupiter.api.Test

class DeepListTest {
    /**
     * Test using a list within a list.
     */
    @Test
    fun testListCommand() {
        SimpleTest().comparisonTest(
            "deepListTest01.txt", "deepListTest.txt",
            "deepListTestExpected.txt"
        )
    }
}
