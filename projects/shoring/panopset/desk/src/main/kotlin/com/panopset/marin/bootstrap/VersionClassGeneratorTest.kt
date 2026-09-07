package com.panopset.marin.bootstrap

import com.panopset.desk.DeployProperties
import java.io.File

class VersionClassGeneratorTest {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val projectDirectoryPath = "/home/karl/w/s0"
            val projectDirectory = File(projectDirectoryPath)
            if (!projectDirectory.exists()) {
                println("$projectDirectoryPath does not exist.")
                return
            }
            if (!projectDirectory.isDirectory()) {
                println("$projectDirectoryPath is not a directory.")
                return
            }
            VersionClassGenerator.main(arrayOf(projectDirectoryPath))
        }
    }
}
