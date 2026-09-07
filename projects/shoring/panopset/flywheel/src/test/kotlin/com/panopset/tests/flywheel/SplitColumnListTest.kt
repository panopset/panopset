package com.panopset.tests.flywheel

import com.panopset.compat.Stringop.setEol
import com.panopset.flywheel.FlywheelBuilder
import com.panopset.flywheel.LINE_BREAKS
import com.panopset.flywheel.ReservedWords
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SplitColumnListTest {
    /**
     * Test split columns in a list command. See **splitList.txt**.
     */
    @Test
    fun testSplitColumns() {
        setEol("\n")
        val expected = "6#Zonk7#Bonk"
        val inp = arrayOf<String?>("\${@l splitList.txt}\${2}#\${3}\${@q}")
        val results = FlywheelBuilder().withLineFeedRules(LINE_BREAKS) // TODO use testing framework.
            .baseDirectoryPath("src/test/resources/com/panopset/tests/flywheel")
            .map(ReservedWords.SPLITS, "13,14").inputArray(inp).construct().exec()
        Assertions.assertEquals(expected, results)
    }
}
