package com.panopset.compat

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TextScramblerTest {
    private val txtLong = "This is not very long but long enough"
    private val txtShort = "FOO"
    private val txtRandom = "" + nextLong()

    @Test
    fun testScrambler() {
        val ts = TextScrambler()
        testScramblerOn(ts, txtShort)
        testScramblerOn(ts, txtLong)
        testScramblerOn(ts, txtRandom)
        testScramblerOn(ts, TEN_MILES)
        Assertions.assertEquals(TEN_MILES, ts.decrypt(TEST_PWD, MANY_HAPPY_RETURNS))
    }

    private fun testScramblerOn(ts: TextScrambler, txt0: String) {
        val txt1 = txt0 + "a"
        val scrt0 = ts.encrypt(TEST_PWD, txt0)
        val scrt1 = ts.encrypt(TEST_PWD, txt1)
        Assertions.assertFalse(scrt0 == txt0)
        Assertions.assertFalse(scrt1 == txt1)
        Assertions.assertFalse(scrt0.take(20) == scrt1.take(20))
        val rslt0 = ts.decrypt(TEST_PWD, scrt0)
        val rslt1 = ts.decrypt(TEST_PWD, scrt1)
        Assertions.assertEquals(rslt0, txt0)
        Assertions.assertEquals(rslt1, txt1)
    }

    companion object {
        private const val TEST_PWD = "4traN"
        private const val TEN_MILES = "ten miles"
        private const val MANY_HAPPY_RETURNS = ("FRqy4hq6gPqAWnN-D-gQ3wQG"
                + Stringop.DOS_RTN + "pPsEzqGA7yu7F8sczbGBMM8_wmeBZg==\n" + Stringop.DOS_RTN)
    }
}
