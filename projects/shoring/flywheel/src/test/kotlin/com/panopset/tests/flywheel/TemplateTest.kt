package com.panopset.tests.flywheel

import org.junit.jupiter.api.Test

class TemplateTest {
    /**
     * Test template command. See **templateTest01.txt**.
     */
    @Test
    fun testTemplateCommand() {
        SimpleTest().comparisonTest(
            "templateTest01.txt", "templateTest.txt",
            "templateTestExpected.txt"
        )
    }
}
