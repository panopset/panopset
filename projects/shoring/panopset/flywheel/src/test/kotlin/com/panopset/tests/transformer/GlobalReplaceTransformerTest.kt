package com.panopset.tests.transformer

import com.panopset.compat.Fileop
import com.panopset.gp.GlobalReplaceProcessor
import com.panopset.gp.PriorAndReplacementLineMustContainFilter
import org.junit.jupiter.api.Assertions
import java.io.IOException

class GlobalReplaceTransformerTest(
    private val searchStr: String, private val replacementStr: String, private val priorLineMustContain: String,
    packageName: String, refreshFile: String, fromToFile: String, expectedFile: String
) : SameFileTest(packageName, refreshFile, fromToFile, expectedFile) {

    override fun xform(): String {
        try {
            grp.process(PriorAndReplacementLineMustContainFilter
                (priorLineMustContain, "", searchStr, replacementStr))
            return Fileop.readTextFile(tempFile)
        } catch (e: IOException) {
            Assertions.fail<Any>("performTransformation failure.", e)
        }
        return ""
    }

    private var grp = GlobalReplaceProcessor(tempFile, "txt", false)
}
