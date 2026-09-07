package com.panopset.flywheel

import com.panopset.compat.Fileop
import java.io.File

class TemplateFile(file: File) : TemplateArray(Fileop.readLines( file)) {
    override val name: String = Fileop.getCanonicalPath(file)
}
