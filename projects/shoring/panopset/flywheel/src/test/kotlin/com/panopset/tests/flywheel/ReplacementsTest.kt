package com.panopset.tests.flywheel

import com.panopset.compat.Stringop
import com.panopset.flywheel.FlywheelBuilder
import com.panopset.flywheel.LFR_FLATTEN
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class ReplacementsTest {
    @Test
    fun testReplacements() {
        Assertions.assertEquals(
            Stringop.BAR,
            FlywheelBuilder().replacement(Stringop.FOO, Stringop.BAR)
                .withLineFeedRules(LFR_FLATTEN).inputArray(arrayOf(Stringop.FOO)).construct()
                .exec()
        )
    }

    @Test
    fun testReplacementsUsingProps() {
        val props = Properties()
        props[Stringop.FOO] = Stringop.BAR
        Assertions.assertEquals(
            Stringop.BAT,
            FlywheelBuilder().properties(props).withLineFeedRules(LFR_FLATTEN)
                .replacement(Stringop.BAR, Stringop.BAT).inputArray(arrayOf(SCRIPT)).construct()
                .exec()
        )
    }

    companion object {
        private const val SCRIPT = "\${foo}"
    }
}
