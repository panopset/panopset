package com.panopset.marin.bootstrap

import com.panopset.compat.Fileop
import com.panopset.compat.Logz
import com.panopset.compat.Stringop.isPopulated
import com.panopset.compat.Stringop.replaceLine
import com.panopset.desk.DeployProperties
import com.panopset.flywheel.FlywheelBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class VersionClassGenerator(private val srcDirectory: String, private var versionString: String) {
    val panopsetSrcPath = "$srcDirectory/projects/shoring/panopset"

    private fun updateVersion() {
        if (updateAppVersionClass()) {
            updatePoms()
        }
    }

    private fun updatePoms() {
        updatePom(1,"$panopsetSrcPath/pom.xml")
        updateShoringProject("compat")
        updateShoringProject("desk")
        updateShoringProject("flywheel")
        updateShoringProject("fxapp")
        updatePom(2,"$srcDirectory/projects/beam/pom.xml")
        updatePom(1,"$srcDirectory/../fas21/projects/shoring/fas21/pom.xml")
        updatePom(1,"$srcDirectory/../fas21/projects/shoring/fas21/fsbengine/pom.xml")
        updatePom(1,"$srcDirectory/../fas21/projects/shoring/fas21/fsbdesk/pom.xml")
    }

    private fun updateShoringProject(project: String) {
        val pp = "$panopsetSrcPath/$project/pom.xml"
        updatePom(1, pp)
    }

    private fun updatePom(
        expectedOccurrenceToReplaceNaturalNumber: Int,
        pp: String
    ) {
        println("Updating: $pp")
        val fr = "<version>"
        val tm = "<version>%s</version>"
        val vr = getVersionString()
        replacePomLine(pp, fr, expectedOccurrenceToReplaceNaturalNumber,
            String.format(tm, vr))
    }

    private fun replacePomLine(
        path: String, lineToReplaceContaining: String,
        expectedOccurrenceToReplaceNaturalNumber: Int,
        fullReplacementLine: String
    ) {
        val file = File(path)
        if (!file.exists()) {
            throw RuntimeException("File does not exist: $path")
        }
        val source = Fileop.readLines( file)
        val strs = replaceLine(
            source,
            lineToReplaceContaining, expectedOccurrenceToReplaceNaturalNumber, fullReplacementLine
        )
        if (source == strs) {
            Logz.info(String.format("No changes to %s, skipping...", path))
        } else {
            Fileop.write( strs, file)
            Logz.info(String.format("%s, updated with %s.", path, fullReplacementLine))
        }
    }

    private fun updateAppVersionClass(): Boolean {
        val vf = File("$srcDirectory/projects/shoring/panopset/compat/src/main/kotlin/com/panopset/compat/AppVersion.kt")
        if (!vf.exists()) {
            Logz.errorMsg("File not found", vf)
            return false
        }
        if (!vf.parentFile.exists()) {
            Logz.errorMsg("Parent directory not found", vf)
            return false
        }
        val result = FlywheelBuilder()
            .scriptFilePath(
                "$srcDirectory/docs/templates/version.tmplt"
            )
            .map("panopset_desk_version", getVersionString())
            .map("panopset_desk_build", getBuildString())
            .construct().exec()
        if (isPopulated(result)) {
            Fileop.write( result, vf)
        }
        return true
    }

    private fun getBuildString(): String {
        return SimpleDateFormat("yyyyMMddHHmm").format(Date())
    }

    private fun getVersionString(): String {
        return versionString
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val srcDirectory = if (args.isEmpty()) {
                "."
            } else {
                args[0]
            }
            VersionClassGenerator(srcDirectory, DeployProperties().getPanopsetVersion()).updateVersion()
        }
    }
}
