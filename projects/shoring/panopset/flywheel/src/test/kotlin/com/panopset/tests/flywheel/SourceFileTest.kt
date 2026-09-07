package com.panopset.tests.flywheel

import com.panopset.flywheel.FlywheelBuilder
import com.panopset.flywheel.SourceFile
import com.panopset.tests.TEST_DIRECTORY
import com.panopset.tests.transformer.StandardPackagePath
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SourceFileTest {
    @Test
    fun testSourceFileConstructor() {
        val file = StandardPackagePath(javaClass.packageName).getFile(SimpleTest.SIMPLETEST)
        val flywheel = FlywheelBuilder()
            .targetDirectory(TEST_DIRECTORY).file(file).construct()
        val sf00 = SourceFile(flywheel, SimpleTest.SIMPLETEST)
        val sf01 = SourceFile(file, flywheel.getBaseDirectoryPath())
        Assertions.assertTrue(sf00 == sf01)
        Assertions.assertTrue(sf00.isValid)
        flywheel.exec()
    }
}
