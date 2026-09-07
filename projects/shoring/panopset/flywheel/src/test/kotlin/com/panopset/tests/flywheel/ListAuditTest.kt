package com.panopset.tests.flywheel

import com.panopset.flywheel.ListAudit
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class ListAuditTest {
    @Test
    fun testListAudit() {
        val la = ListAudit()
        la.add("foo", Arrays.asList(*arrayOf("a", "b")))
        la.add("bar", Arrays.asList(*arrayOf("b", "c")))
        la.add("zlist", Arrays.asList(*arrayOf("x", "y")))
        la.add("alist", Arrays.asList(*arrayOf("b", "a")))
        val report = la.report
        Assertions.assertEquals(6, report.size)
        Assertions.assertEquals(",alist,bar,foo,zlist", report[0])
        Assertions.assertEquals("a,*,,*,", report[1])
        Assertions.assertEquals("b,*,*,*,", report[2])
        Assertions.assertEquals("c,,*,,", report[3])
        Assertions.assertEquals("x,,,,*", report[4])
        Assertions.assertEquals("y,,,,*", report[5])
    }
}
