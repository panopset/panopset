package com.panopset.tests.flywheel

import com.panopset.compat.Fileop
import com.panopset.flywheel.FlywheelBuilder
import com.panopset.gp.FileCompare
import com.panopset.tests.TEST_DIRECTORY
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class SimpleTest {
    @Test
    fun testSimpleScript() {
        SimpleTest().comparisonTest(SIMPLETEST, SIMPLEOUT, EXPECTED)
    }

    fun comparisonTest(scriptName: String?, generatedFileName: String, expected: String) {
        comparisonTest(scriptName, arrayOf(generatedFileName), arrayOf(expected))
    }

    fun comparisonTest(scriptName: String?, generatedFileNames: Array<String>, expecteds: Array<String>) {
        Assertions.assertEquals(expecteds.size, generatedFileNames.size)
        for ((incr, generatedFileName) in generatedFileNames.withIndex()) {
            val generatedFile = File(Fileop.combinePaths(TEST_DIRECTORY, generatedFileName))
            if (generatedFile.exists()) {
                Fileop.delete(generatedFile)
            }
            val script = FlywheelBuilder().targetDirectory(TEST_DIRECTORY)
                .file(File(String.format("%s%s", TEST_FILE_PATH, scriptName))).construct()
            script.exec()
            Assertions.assertFalse(script.isStopped)
            Assertions.assertTrue(generatedFile.exists())
            Assertions.assertTrue(
                FileCompare.filesAreSame(
                    generatedFile,
                    File(String.format("%s%s", TEST_FILE_PATH, expecteds[incr]))
                )
            )
        }
    }

    companion object {
        // TODO
        const val TEST_FILE_PATH = "src/test/resources/com/panopset/tests/flywheel/"
        const val SIMPLETEST = "simpleTest.txt"
        const val SIMPLEOUT = "simpleOut.txt"
        const val EXPECTED = "simpleTestExpected.txt"
    }
}
