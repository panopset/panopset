package com.panopset.tests.marin

import com.panopset.desk.DeployProperties
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class VersionLoadTester {

    @Test
    fun testLoadProps() {
        Assertions.assertTrue(DeployProperties().get("FXV").length > 2)
    }
}
