package com.panopset.desk.utilities

import com.panopset.compat.AppVersion
import com.panopset.compat.Fileop
import com.panopset.compat.Jsonop
import com.panopset.compat.Propop
import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.marin.secure.checksums.ChecksumType
import java.io.File
import java.io.StringWriter
import java.util.*

class GenerateDownloadsTable(
    val gcs: GrandCentralStation,
) {
    val projectDir = gcs.projectDir

    fun createDownloadsTable(path: String): String {
        val version = AppVersion.getFullVersion()
        val deployPropsFile = File(Fileop.combinePaths(projectDir, "deploy.properties"))
        val deployProps = Propop.load(deployPropsFile)
        val sampleApp = deployProps["SPLAPP"]
        val platformDownloadMap = createPlatformDownloadMap(path)
        val sw = StringWriter()
        sw.append("<table>")
        for ((_, value) in platformDownloadMap) {
            val platformName = value.platformName
            val platformPropertiesFileName = "app$platformName.properties"
            val propsFile = File(Fileop.combinePaths(projectDir, platformPropertiesFileName))
            val props = Propop.load(propsFile)
            val platformFullName = props.getProperty("PLATFORM_NAME").replace("\"", "")
            val launchPath = props.getProperty("LAUNCH_PATH").replace("\"", "")
            val javaCmd = props.getProperty("JAVA_CMD").replace("\"", "")
            sw.append("<tr class=\"menuBar\"><td colspan=\"5\"><b>$platformFullName</b></td></tr>\n")
            sw.append("<tr><th>Type</th><th>Download</th><th>Version</th><th>Bytes</th>")
            sw.append("<th>SHA-512</th></tr>")
            var firstTime = true
            for ((artifactType, platformShortKey, artifactName, byteCount, sha512) in value.platformDownloads) {
                sw.append("\n\n<tr><td nowrap>\n")
                sw.append(artifactType)
                sw.append("</td><td nowrap>\n")
                sw.append("<a href=\"/downloads/$platformShortKey/$artifactType/$artifactName\">$artifactName</a>")
                sw.append("</td><td>\n")
                sw.append(version)
                sw.append("</td><td>\n")
                sw.append(byteCount)
                sw.append("</td><td class=\"dsw99\"><input class=\"output2\" type=\"text\" value=\"")
                sw.append(sha512)
                sw.append("\"></input></td></tr>")
                if (firstTime) {
                    sw.append("<tr><td colspan=\"2\"><pre>Launch path:</pre></td><td colspan=\"3\"><pre>$launchPath$sampleApp</pre></td></tr>")
                } else {
                    sw.append("<tr><td colspan=\"2\"><pre>Launch command:</pre></td><td colspan=\"3\"><pre>$javaCmd</pre></td></tr>")
                }
                firstTime = false
            }
            sw.append("<tr><td colspan=\"5\">&nbsp;</td></tr>")
        }
        sw.append("</table>\n\n")
        return sw.toString()
    }

    private fun createPlatformDownloadMap(path: String): MutableMap<String, PlatformDownloadCollection> {
        val platformDownloadMap = Collections.synchronizedSortedMap<String, PlatformDownloadCollection>(TreeMap())
        val tempDirDownloads = File(path)
        return cpdmDownloads(tempDirDownloads, platformDownloadMap)
    }

    private fun cpdmDownloads(
        f: File,
        platformDownloadMap: MutableMap<String, PlatformDownloadCollection>,
    ): MutableMap<String, PlatformDownloadCollection> {
        for (f in f.listFiles()!!) {
            if (!f.isDirectory) {
                return platformDownloadMap
            }
            cpdmPlatforms(f, platformDownloadMap)
        }
        return platformDownloadMap
    }

    private fun cpdmPlatforms(f: File, platformDownloadMap: MutableMap<String, PlatformDownloadCollection>)
    : MutableMap<String, PlatformDownloadCollection> {
        if (!f.isDirectory) {
            return platformDownloadMap
        }
        val platformShortKey = f.name
        for (f in f.listFiles()!!) {
            val artifactType = f.name
            cpdmArtifactTypes(f, platformShortKey, artifactType, platformDownloadMap)
        }
        return platformDownloadMap
    }

    private fun cpdmArtifactTypes(f: File, platformShortKey: String, artifactType: String,
                                  platformDownloadMap: MutableMap<String, PlatformDownloadCollection>)
    : MutableMap<String, PlatformDownloadCollection> {
        if (!f.isDirectory) {
            return platformDownloadMap
        }
        for (f in f.listFiles()!!) {
            if (f.isFile) {
                cpdmDownloadEntry(f, platformShortKey, artifactType, platformDownloadMap)
            }
        }
        return platformDownloadMap
    }

    private fun cpdmDownloadEntry(f: File, platformShortKey: String, artifactType: String,
                                  platformDownloadMap: MutableMap<String, PlatformDownloadCollection>)
    : MutableMap<String, PlatformDownloadCollection> {
        val jsonStr = Fileop.readTextFile(f)
        val rawMap = Jsonop<HashMap<String, String>>().fromJson(jsonStr, HashMap<String, String>().javaClass)
        val map = Collections.synchronizedSortedMap(TreeMap<String, String>())
        for ((key, value) in rawMap) {
            map[key] = value
        }
        val ifn = map["ifn"] ?: return platformDownloadMap
        val platformKey = map["platformKey"] ?: return platformDownloadMap
        val archProps = loadPropsFor(platformKey)
        val platformDisplayOrder = archProps.getProperty("DSPORD")

        val bytes = map["bytes"] ?: return platformDownloadMap
        val sha512 = map[ChecksumType.SHA512.key] ?: return platformDownloadMap

        addPlatformIfNecessary(platformDisplayOrder, platformKey, platformDownloadMap).platformDownloads.add(
            PlatformDownload(artifactType, platformShortKey,ifn, bytes, sha512)
        )
        return platformDownloadMap
    }

    private fun loadPropsFor(fxArch: String): Properties {
        val propsFile = File("app$fxArch.properties")
        if (!propsFile.exists()) {
            throw RuntimeException(propsFile.canonicalPath)
        }
        return Propop.load(propsFile)
    }
}

private fun addPlatformIfNecessary(
    platformDisplayOrder: String,
    platformKey: String,
    platformDownloadCollectionMap: MutableMap<String, PlatformDownloadCollection>
): PlatformDownloadCollection {
    if (platformDownloadCollectionMap.containsKey(platformDisplayOrder)) {
        return platformDownloadCollectionMap[platformDisplayOrder]!!
    }
    val rtn = PlatformDownloadCollection(platformDisplayOrder, platformKey)
    platformDownloadCollectionMap[platformDisplayOrder] = rtn
    return rtn
}

private data class PlatformDownload(
    val artifactType: String,
    val platformShortKey: String,
    val artifactName: String,
    val byteCount: String,
    val sha512: String,
)

private data class PlatformDownloadCollection(
    val platformDisplayOrder: String,
    val platformName: String
) : Comparable<PlatformDownloadCollection> {
    val platformDownloads = ArrayList<PlatformDownload>()
    override fun compareTo(other: PlatformDownloadCollection): Int {
        return this.platformDisplayOrder.compareTo(other.platformDisplayOrder)
    }
}
