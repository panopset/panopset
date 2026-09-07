package com.panopset.tests.flywheel

import com.panopset.compat.Stringop.setEol
import com.panopset.flywheel.FlywheelBuilder
import com.panopset.flywheel.LINE_BREAKS
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class EscapeTest {
    @Test
    fun testEscapeNewLine() {
        Assertions.assertEquals(
            "foobar",
            applyTemplate(Arrays.asList(*arrayOf("foo\\", "bar")))
        )
        Assertions.assertEquals("", applyTemplate(Arrays.asList(*arrayOf("\\", ""))))
        Assertions.assertEquals("\\\n", applyTemplate(Arrays.asList(*arrayOf("\\\\", ""))))
        Assertions.assertEquals("\\", applyTemplate(Arrays.asList(*arrayOf("\\\\"))))
        Assertions.assertEquals("foobar", applyTemplate(Arrays.asList(*arrayOf("\${@p x}foo\\", "bar\${@q}\${x}"))))
    }

    private fun applyTemplate(input: List<String>): String {
        setEol("\n")
        val fw =
            FlywheelBuilder().inputList(input).withLineFeedRules(LINE_BREAKS).construct()
        return fw.exec()
    }
}
