package com.panopset.tests.flywheel

import org.junit.jupiter.api.Test

class VariableNamedFilesTest {
    /**
     * Test variable named template. See **variableNamedTemplateTest.txt**.
     */
    @Test
    fun testVariableNamedTemplate() {
        SimpleTest().comparisonTest(
            "variableNamedTemplateTest.txt", "variableNamedTemplateResult.txt",
            "variableNamedTemplateExpected.txt"
        )
    }

    /**
     * Test variable named list. See **variableNamedListTest.txt**
     */
    @Test
    fun testVariableNamedList() {
        SimpleTest().comparisonTest(
            "variableNamedListTest.txt", "variableNamedListResult.txt",
            "variableNamedListExpected.txt"
        )
    }
}
