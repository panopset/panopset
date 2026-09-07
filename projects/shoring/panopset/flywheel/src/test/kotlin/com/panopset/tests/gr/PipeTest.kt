package com.panopset.tests.gr

import com.panopset.tests.transformer.GlobalReplaceTransformerTest
import org.junit.jupiter.api.Test
import java.io.IOException

class PipeTest {
    @Test
    @Throws(IOException::class)
    fun test() {
        GlobalReplaceTransformerTest(
            "red", "yellow", "blue", this.javaClass.getPackageName(), REFRESH, TEMPLATE,
            EXPECTED
        ).test()
    }

    companion object {
        private const val REFRESH = "pipeTestRefresh.txt"
        private const val TEMPLATE = "pipeTest.txt"
        private const val EXPECTED = "pipeTestExpected.txt"
    }
}
