package com.panopset.tests.flywheel

import com.panopset.flywheel.FlywheelBuilder
import com.panopset.flywheel.LineFeedRules
import com.panopset.tests.transformer.FlywheelTemplateToFileTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ReplacementTest {
    @Test
    fun testReplacements() {
        val fttft = FlywheelTemplateToFileTest(
            javaClass.getPackageName(),
            TEST_FILE_NAME,
            TEST_FILE_NAME,
            "replacementsTestExpected.txt"
        )
        val fb = fttft.flywheelBuilder
        fb.replacement("foo", "bar")
        fttft.test()
        //  Flywheel flywheel = fttft.getFlywheel();
//  File generatedFile = new File(SimpleTest.TEST_DIRECTORY + "/" + TEST_FILE_NAME);
//  if (generatedFile.exists()) {
//   Fileop.delete(generatedFile);
//  }
//  Flywheel script = new FlywheelBuilder().targetDirectory(SimpleTest.TEST_DIRECTORY)
//    .file(new File(SimpleTest.TEST_FILE_PATH + TEST_FILE_NAME)).replacement("foo", "bar").construct();
//  script.exec();
// 	assertFalse(flywheel.isStopped());
//		Logop.dspmsg(Fileop.getCanonicalPath(generatedFile));
//		assertTrue(generatedFile.exists());
//		assertTrue(generatedFile.length() > 0);
//		assertTrue(FileCompare.filesAreSame(generatedFile,
//				new File(SimpleTest.TEST_FILE_PATH + "replacementsTestExpected.txt")));
    }

    @Test
    fun testAtReplacement() {
        val f = FlywheelBuilder().inputString("\${@p x}\${@\${@q}\${x}q}")
            .withLineFeedRules(LineFeedRules(false, false)).construct()
        Assertions.assertEquals("\${@q}", f.exec())
    }

    companion object {
        private const val TEST_FILE_NAME = "replacementsTest.txt"
    }
}
