package com.panopset.flywheel

import com.panopset.compat.Fileop.getCanonicalPath
import com.panopset.compat.Fileop.readLines
import java.io.File

/**
 * Flywheel source file, all paths relative to the primary script file.
 */
class SourceFile() {
    lateinit var relativePath: String
    lateinit var file: File
    lateinit var canonicalPath: String
    private var sourceLines: List<String> = ArrayList()

    /**
     * Directory depth relative to driver template.
     */
    var depth: Int? = null
        /**
         * Get depth relative to driver template.
         *
         * @return Directory depth relative to driver template
         */
        get() {
            if (field == null) {
                field = 0
                var temp = relativePath
                var idx = temp.indexOf("/")
                while (idx > -1) {
                    field = field!! + 1
                    temp = temp.substring(idx + 1)
                    idx = temp.indexOf("/")
                }
            }
            return field
        }
        private set

    /**
     * Reset source lines, for list processing.
     */
    fun reset() {
        sourceLines = ArrayList()
    }

    /**
     * Get source lines.
     *
     * @return Source lines.
     */
    fun getTheSourceLines(): List<String> {
        if (sourceLines.isEmpty()) {
            sourceLines = readLines( file)
        }
        return sourceLines
    }

    /**
     * SourceFile constructor.
     *
     * @param sourceFile Source file.
     * @param basePath Base directory path.
     */
    constructor(sourceFile: File, basePath: String): this() {
        file = sourceFile
        canonicalPath = getCanonicalPath(file)
        relativePath = canonicalPath.substring(basePath.length + 1)
    }

    /**
     * SourceFile constructor.
     *
     * @param sourceFileRelativePath Source file relative path.
     * @param flywheel Flywheel.
     */
    constructor(flywheel: Flywheel, sourceFileRelativePath: String): this() {
        relativePath = sourceFileRelativePath
        file = File(flywheel.getBaseDirectoryPath() + "/" + relativePath)
        canonicalPath = getCanonicalPath(file)
    }

    val isValid: Boolean
        /**
         * Check to see if source file exists, and can be read.
         *
         * @return true if source file exists.
         */
        get() = file.exists() && file.canRead()

    /**
     * Override equals method. From
     * [
 * http://www.geocities.com/technofundo/tech/java/equalhash.html ](http://www.geocities.com/technofundo/tech/java/equalhash.html)
     *
     * @param other Object.
     * @return true if obj is equal to this.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || other.javaClass != this.javaClass) {
            return false
        }
        val test = other as SourceFile
        return canonicalPath == test.canonicalPath && relativePath == test.relativePath
    }

    /**
     * @return hash code for this object.
     */
    override fun hashCode(): Int {
        return file.hashCode()
    }
}
