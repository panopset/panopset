package com.panopset.tests.flywheel

import com.panopset.compat.Stringop.BAR
import com.panopset.compat.Stringop.FOO
import com.panopset.flywheel.FlywheelBuilder
import com.panopset.flywheel.LFR_FLATTEN
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Tests the Flywheel template variable resolution.
 */
class BracketTest {
    @Test
    fun testBracket() {
        val s = FlywheelBuilder().withLineFeedRules(LFR_FLATTEN).map(FOO, BAR)
            .inputArray(arrayOf(INPUT)).construct().exec()
        Assertions.assertEquals(EXPECTED, s)
    }
}

val INPUT = "$&#123;1} \${$FOO}."

/**
 * **$&#123;1} bar**.
 */
val EXPECTED = "$&#123;1} $BAR."

