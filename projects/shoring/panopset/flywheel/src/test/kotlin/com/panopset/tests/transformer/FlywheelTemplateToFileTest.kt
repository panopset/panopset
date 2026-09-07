package com.panopset.tests.transformer

import com.panopset.compat.Fileop
import com.panopset.flywheel.Flywheel
import com.panopset.flywheel.FlywheelBuilder
import com.panopset.tests.TEST_DIRECTORY
import java.io.File

class FlywheelTemplateToFileTest(
    packageRelPath: String, fromFileRezPath: String, private val tempFileName: String,
    private val expectedFileRezPath: String
) : StandardTransformerTest(packageRelPath) {
    override fun createResultsDataSupplier(): ResultsDataSupplier {
        return object : ResultsDataSupplier {
            override val expectedResults: String
                get() = getExpectedResults()
            override val actualResults: String
                get() = getActualResults()

        }
    }

    var flywheelBuilder: FlywheelBuilder = FlywheelBuilder().targetDirectory(TEST_DIRECTORY)
                    .file(StandardPackagePath(packageName).getFile(fromFileRezPath))
    var flywheel: Flywheel = flywheelBuilder.construct()

    private fun getActualResults(): String {
        flywheel.exec()
        val generatedFile = File(
            Fileop.combinePaths(
                TEST_DIRECTORY,
                tempFileName
            )
        )
        return Fileop.readTextFile(generatedFile)
    }

    private fun getExpectedResults(): String {
        val expectedFile = StandardPackagePath(packageName).getFile(expectedFileRezPath)
        return Fileop.readTextFile(expectedFile)
    }
 }
