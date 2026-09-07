package com.panopset.tests

import com.panopset.compat.Fileop
import java.io.File

val TEST_DIRECTORY = File(
    Fileop.combinePaths(Fileop.getCanonicalPath(Fileop.TEMP_DIRECTORY), "/com/panopset/test")
)
