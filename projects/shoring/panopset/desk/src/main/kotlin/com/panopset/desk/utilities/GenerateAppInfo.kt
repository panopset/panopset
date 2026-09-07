package com.panopset.desk.utilities

import com.panopset.compat.*
import com.panopset.compat.Fileop.checkParent
import com.panopset.marin.bootstrap.PlatformMap
import com.panopset.marin.secure.checksums.ChecksumType
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.collections.HashMap

class GenerateAppInfo {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            GenerateAppInfo().genappinf(args[0])
        }
    }

    fun genappinf(platformPropsFileBaseName: String): String {
        val propsFileName = "${platformPropsFileBaseName}.properties"
        val propsFile = File(propsFileName)
        if (!propsFile.exists()) {
            throw Exception("Props file ${propsFile.canonicalPath} not found!")
        }
        val props = Propop.load(propsFile)
        if (props.isEmpty) {
            throw Exception("Props file ${propsFile.canonicalPath} is empty!")
        }
        generateJsonForInstaller(props)
        createTheStandaloneZipFile(props)
        return ""
    }
}

private fun createTheStandaloneZipFile(props: Properties) {
    val platformKey = props.getProperty("PLATFORM_KEY")
    if (platformKey == null) {
        Logz.errorMsg("PLATFORM_KEY is missing!")
        return
    }
    val arn = if (platformKey == "mac") {
        "panopset.app"
    } else {
        "panopset"
    }
    val standAloneDirectory = Paths.get("target/standalone/$arn").toFile()
    val standAloneZip = Paths.get("target/standalone/$arn.zip").toFile()
    val standAloneZipJson = Paths.get("target/standalone/$arn.zip.json").toFile()
    zipDirectory(standAloneDirectory.toPath(), standAloneZip.toPath())
    val json = Jsonop<Map<String, String>>().toJson(
        createJsonMap(
            platformKey,
            standAloneZip)
        )
    Fileop.write(json, standAloneZipJson)
}

private fun generateJsonForInstaller(props: Properties) {
    val platformKey = props.getProperty("PLATFORM_KEY")
    val platformInstallerPath = "./target/installer"
    val platformInstallerDir = File(platformInstallerPath)
    var firstTime = true
    if (platformInstallerDir.exists() && platformInstallerDir.isDirectory) {
        if (platformInstallerDir.listFiles() == null) {
            throw Exception("${platformInstallerDir.canonicalPath} is empty!")
        }
        for (file in platformInstallerDir.listFiles()!!) {
            if (firstTime && file.exists() && !file.isDirectory) {
                val json = Jsonop<Map<String, String>>().toJson(
                    createJsonMap(
                        platformKey,
                        file
                    )
                )
                Fileop.write(json, File("${platformInstallerPath}${fsp}${file.name}.json"))
                firstTime = false
            }
        }
    }
}

fun createJsonMap(platformKey: String, installerFile: File): Map<String, String> {
    val map = HashMap<String, String>()
    map["platformKey"] = platformKey
    map["version"] = AppVersion.getFullVersion()
    map["bytes"] = byteCount(installerFile)
    map[ChecksumType.SHA512.key] = sha512(installerFile)
    map["ifn"] = installerFile.name
    return map
}

private fun zipDirectory(sourceDirectory: Path, targetZip: Path) {
    val preserveUnixModes = shouldPreserveUnixModes()
    val unixModes = HashMap<String, Int>()
    Files.createDirectories(targetZip.parent)
    ZipOutputStream(Files.newOutputStream(targetZip)).use { output ->
        Files.walk(sourceDirectory).use { paths ->
            paths
                .filter { it != sourceDirectory }
                .sorted()
                .forEach { path ->
                    val relativePath = sourceDirectory.relativize(path).toString().replace(File.separatorChar, '/')
                    val entryName = if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) "$relativePath/" else relativePath
                    if (preserveUnixModes) {
                        unixModes[entryName] = getUnixMode(path)
                    }
                    output.putNextEntry(ZipEntry(entryName))
                    if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
                        Files.copy(path, output)
                    }
                    output.closeEntry()
                }
        }
    }
    if (preserveUnixModes) {
        applyUnixModes(targetZip, unixModes)
    }
}

private fun shouldPreserveUnixModes(): Boolean =
    !System.getProperty("os.name").lowercase(Locale.getDefault()).startsWith("windows") &&
        FileSystems.getDefault().supportedFileAttributeViews().contains("posix")

private fun getUnixMode(path: Path): Int {
    val permissions = Files.getPosixFilePermissions(path, LinkOption.NOFOLLOW_LINKS)
    val typeBits = when {
        Files.isSymbolicLink(path) -> UNIX_MODE_SYMBOLIC_LINK
        Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS) -> UNIX_MODE_DIRECTORY
        else -> UNIX_MODE_REGULAR_FILE
    }
    return typeBits or permissionsToBits(permissions)
}

private fun permissionsToBits(permissions: Set<PosixFilePermission>): Int {
    var mode = 0
    if (permissions.contains(PosixFilePermission.OWNER_READ)) mode = mode or 0b100_000_000
    if (permissions.contains(PosixFilePermission.OWNER_WRITE)) mode = mode or 0b010_000_000
    if (permissions.contains(PosixFilePermission.OWNER_EXECUTE)) mode = mode or 0b001_000_000
    if (permissions.contains(PosixFilePermission.GROUP_READ)) mode = mode or 0b000_100_000
    if (permissions.contains(PosixFilePermission.GROUP_WRITE)) mode = mode or 0b000_010_000
    if (permissions.contains(PosixFilePermission.GROUP_EXECUTE)) mode = mode or 0b000_001_000
    if (permissions.contains(PosixFilePermission.OTHERS_READ)) mode = mode or 0b000_000_100
    if (permissions.contains(PosixFilePermission.OTHERS_WRITE)) mode = mode or 0b000_000_010
    if (permissions.contains(PosixFilePermission.OTHERS_EXECUTE)) mode = mode or 0b000_000_001
    return mode
}

private fun applyUnixModes(targetZip: Path, unixModes: Map<String, Int>) {
    val bytes = Files.readAllBytes(targetZip)
    val endOfCentralDirectoryOffset = findEndOfCentralDirectoryOffset(bytes)
    val centralDirectoryOffset = readIntLittleEndian(bytes, endOfCentralDirectoryOffset + EOCD_CENTRAL_DIRECTORY_OFFSET)
    var offset = centralDirectoryOffset
    while (offset + CENTRAL_DIRECTORY_HEADER_SIZE <= bytes.size) {
        if (readIntLittleEndian(bytes, offset) != CENTRAL_DIRECTORY_SIGNATURE) {
            break
        }
        val fileNameLength = readShortLittleEndian(bytes, offset + CENTRAL_DIRECTORY_FILE_NAME_LENGTH_OFFSET)
        val extraFieldLength = readShortLittleEndian(bytes, offset + CENTRAL_DIRECTORY_EXTRA_FIELD_LENGTH_OFFSET)
        val commentLength = readShortLittleEndian(bytes, offset + CENTRAL_DIRECTORY_COMMENT_LENGTH_OFFSET)
        val fileNameOffset = offset + CENTRAL_DIRECTORY_HEADER_SIZE
        val fileName = String(bytes, fileNameOffset, fileNameLength, StandardCharsets.UTF_8)
        unixModes[fileName]?.let { unixMode ->
            bytes[offset + CENTRAL_DIRECTORY_VERSION_MADE_BY_HOST_OFFSET] = ZIP_HOST_SYSTEM_UNIX.toByte()
            val externalAttributes = (unixMode shl 16) or if (fileName.endsWith("/")) ZIP_DIRECTORY_ATTRIBUTE else 0
            writeIntLittleEndian(bytes, offset + CENTRAL_DIRECTORY_EXTERNAL_ATTRIBUTES_OFFSET, externalAttributes)
        }
        offset += CENTRAL_DIRECTORY_HEADER_SIZE + fileNameLength + extraFieldLength + commentLength
    }
    Files.write(targetZip, bytes)
}

private fun findEndOfCentralDirectoryOffset(bytes: ByteArray): Int {
    val minimumOffset = maxOf(0, bytes.size - MAX_END_OF_CENTRAL_DIRECTORY_SEARCH)
    for (offset in bytes.size - END_OF_CENTRAL_DIRECTORY_MIN_SIZE downTo minimumOffset) {
        if (readIntLittleEndian(bytes, offset) == END_OF_CENTRAL_DIRECTORY_SIGNATURE &&
            offset + END_OF_CENTRAL_DIRECTORY_MIN_SIZE + readShortLittleEndian(bytes, offset + EOCD_COMMENT_LENGTH_OFFSET) == bytes.size
        ) {
            return offset
        }
    }
    throw IllegalStateException("Could not find ZIP end of central directory record")
}

private fun readShortLittleEndian(bytes: ByteArray, offset: Int): Int =
    (bytes[offset].toInt() and 0xff) or ((bytes[offset + 1].toInt() and 0xff) shl 8)

private fun readIntLittleEndian(bytes: ByteArray, offset: Int): Int =
    readShortLittleEndian(bytes, offset) or (readShortLittleEndian(bytes, offset + 2) shl 16)

private fun writeIntLittleEndian(bytes: ByteArray, offset: Int, value: Int) {
    bytes[offset] = (value and 0xff).toByte()
    bytes[offset + 1] = ((value ushr 8) and 0xff).toByte()
    bytes[offset + 2] = ((value ushr 16) and 0xff).toByte()
    bytes[offset + 3] = ((value ushr 24) and 0xff).toByte()
}

private const val UNIX_MODE_REGULAR_FILE = 0x8000
private const val UNIX_MODE_DIRECTORY = 0x4000
private const val UNIX_MODE_SYMBOLIC_LINK = 0xA000
private const val ZIP_HOST_SYSTEM_UNIX = 3
private const val ZIP_DIRECTORY_ATTRIBUTE = 0x10
private const val CENTRAL_DIRECTORY_SIGNATURE = 0x02014b50
private const val END_OF_CENTRAL_DIRECTORY_SIGNATURE = 0x06054b50
private const val CENTRAL_DIRECTORY_HEADER_SIZE = 46
private const val END_OF_CENTRAL_DIRECTORY_MIN_SIZE = 22
private const val MAX_END_OF_CENTRAL_DIRECTORY_SEARCH = 65_557
private const val CENTRAL_DIRECTORY_VERSION_MADE_BY_HOST_OFFSET = 5
private const val CENTRAL_DIRECTORY_FILE_NAME_LENGTH_OFFSET = 28
private const val CENTRAL_DIRECTORY_EXTRA_FIELD_LENGTH_OFFSET = 30
private const val CENTRAL_DIRECTORY_COMMENT_LENGTH_OFFSET = 32
private const val CENTRAL_DIRECTORY_EXTERNAL_ATTRIBUTES_OFFSET = 38
private const val EOCD_COMMENT_LENGTH_OFFSET = 20
private const val EOCD_CENTRAL_DIRECTORY_OFFSET = 16

val fsp = Stringop.FSP
val ush = Stringop.USH
