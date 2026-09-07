package com.panopset.tests.flywheel

import com.panopset.compat.Stringop.firstHexDiff
import com.panopset.compat.Stringop.setEol
import com.panopset.flywheel.samples.FlywheelSamples
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FlywheelSamplesTest {
    @Test
    fun testGetSamples() {
        setEol("\n")
        val flywheelSamples = FlywheelSamples().all()
        Assertions.assertTrue(flywheelSamples.size > 1)
        val fs0 = flywheelSamples[1]
        val fs0text = fs0.templateText.trim { it <= ' ' }
        val expected = EXPECTED
        println(firstHexDiff(expected, fs0text))
        Assertions.assertEquals(expected, fs0text)
        Assertions.assertTrue(fs0.lineBreaks)
        Assertions.assertFalse(fs0.listBreaks)
        val fs1 = flywheelSamples[2]
        Assertions.assertFalse(fs1.lineBreaks)
        Assertions.assertFalse(fs1.listBreaks)
    }

    companion object {
        private const val EXPECTED =
            "\${@p replaceme}textToReplaceWithCommandPrefix\${@q}\${@p pfx}\${@\${@q}\${@r replaceme}\${pfx}\${@q}\${l}"
    }
}
