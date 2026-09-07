package com.panopset.tests.transformer

import com.panopset.flywheel.FlywheelBuilder
import java.io.StringWriter

class FlywheelTemplateToTextTransformerTest(
    packageName: String,
    private val templateFileRezPath: String,
    private val expected: String
) : StandardTransformerTest(packageName) {
    override fun createResultsDataSupplier(): ResultsDataSupplier {
        return object : ResultsDataSupplier {
            override val expectedResults: String
                get() = expected
            override val actualResults: String
                get() {
                    val sw = StringWriter()
                    val template = StandardPackagePath(packageName).getFile(templateFileRezPath)
                    val script = FlywheelBuilder().withWriter(sw).file(template)
                        .construct()
                    script.exec()
                    return sw.toString()
                }
        }
    }
}
