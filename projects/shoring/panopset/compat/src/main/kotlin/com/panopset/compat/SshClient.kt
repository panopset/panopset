package com.panopset.compat

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.jcraft.jsch.KeyPair
import com.jcraft.jsch.SftpException
import java.io.File
import java.nio.file.Path
import java.util.Vector

private class SshClient(val session: Session, val channelSftp: ChannelSftp) {
    fun disconnect() {
        channelSftp.disconnect()
        session.disconnect()
    }

    fun get(remotePath: String, localPath: String) {
        channelSftp.connect(5000)
        channelSftp.get(remotePath, localPath)
    }

    fun getDirectory(localDirectory: File, remoteDirectoryPath: String, onlyFilesWithExtensions: List<String>) {
        channelSftp.connect(5000)
        val canonicalLocalDirectory = localDirectory.canonicalFile
        if (canonicalLocalDirectory.exists()) {
            require(canonicalLocalDirectory.isDirectory) { "Local path is not a directory: ${canonicalLocalDirectory.path}" }
        }
        val normalizedExtensions = onlyFilesWithExtensions
            .asSequence()
            .map { it.trim().removePrefix(".").lowercase() }
            .filter { it.isNotEmpty() }
            .toSet()
        downloadDirectory(canonicalLocalDirectory, remoteDirectoryPath, normalizedExtensions)
    }

    fun put(localPath: String, remotePath: String, skipDirectories: List<String>) {
        channelSftp.connect(5000)
        val localFile = File(localPath).canonicalFile
        require(localFile.exists()) { "Local path not found: ${localFile.path}" }
        if (localFile.isDirectory) {
            if (skipDirectories.isNotEmpty() && skipDirectories.contains(localFile.name)) {
                return
            }
            putDirectory(localFile, remotePath, skipDirectories)
        } else {
            channelSftp.put(localFile.path, remotePath)
        }
    }

    private fun putDirectory(localDirectory: File, remoteDirectoryPath: String, skipDirectories: List<String>) {
        ensureRemoteDirectoryExists(remoteDirectoryPath)
        val children = localDirectory.listFiles()
            ?: throw IllegalStateException("Unable to list local directory: ${localDirectory.path}")
        for (child in children) {
            val remoteChildPath = appendRemotePath(remoteDirectoryPath, child.name)
            if (child.isDirectory) {
                if (skipDirectories.isNotEmpty() && skipDirectories.contains(child.name)) {
                    continue
                }
                putDirectory(child, remoteChildPath, skipDirectories)
            } else {
                channelSftp.put(child.canonicalPath, remoteChildPath)
            }
        }
    }

    private fun downloadDirectory(
        localDirectory: File,
        remoteDirectoryPath: String,
        onlyFilesWithExtensions: Set<String>
    ): Boolean {
        val remoteDirectoryAttributes = channelSftp.stat(remoteDirectoryPath)
        require(remoteDirectoryAttributes.isDir) { "Remote path is not a directory: $remoteDirectoryPath" }
        @Suppress("UNCHECKED_CAST")
        val remoteEntries = channelSftp.ls(remoteDirectoryPath) as Vector<ChannelSftp.LsEntry>
        var downloadedAnyFiles = false
        for (entry in remoteEntries) {
            val name = entry.filename
            if (name == "." || name == "..") {
                continue
            }
            val remoteChildPath = appendRemotePath(remoteDirectoryPath, name)
            val localChild = File(localDirectory, name)
            if (entry.attrs.isDir) {
                downloadedAnyFiles = downloadDirectory(localChild, remoteChildPath, onlyFilesWithExtensions) || downloadedAnyFiles
            } else if (hasAllowedExtension(name, onlyFilesWithExtensions)) {
                ensureLocalDirectoryExists(localDirectory)
                channelSftp.get(remoteChildPath, localChild.path)
                downloadedAnyFiles = true
            }
        }
        return downloadedAnyFiles
    }

    private fun ensureLocalDirectoryExists(localDirectory: File) {
        if (localDirectory.exists()) {
            require(localDirectory.isDirectory) { "Local path is not a directory: ${localDirectory.path}" }
            return
        }
        require(localDirectory.mkdirs()) { "Unable to create local directory: ${localDirectory.path}" }
    }

    private fun hasAllowedExtension(fileName: String, onlyFilesWithExtensions: Set<String>): Boolean {
        if (onlyFilesWithExtensions.isEmpty()) {
            return false
        }
        val extension = fileName.substringAfterLast('.', "")
        return extension.isNotEmpty() && onlyFilesWithExtensions.contains(extension.lowercase())
    }

    private fun ensureRemoteDirectoryExists(remoteDirectoryPath: String) {
        if (remoteDirectoryPath.isBlank() || remoteDirectoryPath == "/") return
        val pathParts = remoteDirectoryPath.split("/").filter { it.isNotBlank() }
        var currentPath = if (remoteDirectoryPath.startsWith("/")) "/" else ""
        for (pathPart in pathParts) {
            currentPath = appendRemotePath(currentPath, pathPart)
            try {
                val attrs = channelSftp.stat(currentPath)
                require(attrs.isDir) { "Remote path is not a directory: $currentPath" }
            } catch (e: SftpException) {
                if (e.id != ChannelSftp.SSH_FX_NO_SUCH_FILE) throw e
                channelSftp.mkdir(currentPath)
            }
        }
    }

    private fun appendRemotePath(basePath: String, childPath: String): String {
        return when {
            basePath.isEmpty() -> childPath
            basePath.endsWith("/") -> basePath + childPath
            else -> "$basePath/$childPath"
        }
    }
}

fun opSecureCopyGet(rhd: RemoteHostData, remotePath: String, localPath: String) {
    val sshClient = createSshClient(rhd)
    try {
        sshClient.get(remotePath, localPath)
    } finally {
        sshClient.disconnect()
    }
}

fun opSecureCopyGet(
    rhd: RemoteHostData,
    localDirectory: File,
    remoteDirectoryPath: String,
    onlyFilesWithExtensions: List<String>
) {
    val sshClient = createSshClient(rhd)
    try {
        sshClient.getDirectory(localDirectory, remoteDirectoryPath, onlyFilesWithExtensions)
        Logz.dspmsg("$remoteDirectoryPath sent to ${localDirectory.path}")
    } finally {
        sshClient.disconnect()
    }
}

fun opSecureCopyPut(rhd: RemoteHostData, localPath: String, remotePath: String, skipDirectories: List<String>) {
    val sshClient = createSshClient(rhd)
    try {
        sshClient.put(localPath, remotePath, skipDirectories)
        Logz.dspmsg("$localPath sent to $remotePath")
    } finally {
        sshClient.disconnect()
    }
}

fun opSecureCopyPut(rhd: RemoteHostData, localPath: String, remotePath: String) {
    val sshClient = createSshClient(rhd)
    try {
        sshClient.put(localPath, remotePath, ArrayList())
        Logz.dspmsg("$localPath sent to $remotePath")
    } finally {
        sshClient.disconnect()
    }
}

private fun createSshClient(rhd: RemoteHostData): SshClient {
    val jsch = JSch()
    jsch.instanceLogger = JSchLogger()

    val knownHostsPath = "${Stringop.USH}/.ssh/known_hosts"

    val khf = File(knownHostsPath)
    Logz.dspmsg("knownHostsPath: ${khf.canonicalPath}")

    jsch.setKnownHosts(knownHostsPath);


    val privateKeyPath = rhd.k.replace("~", Stringop.USH)
    val pkf = File(privateKeyPath)
    val pkpcp = pkf.canonicalPath
    jsch.addIdentity(pkpcp)

    val identities = jsch.identityRepository.identities
    Logz.dspmsg("JSch loaded ${identities.size} identity/identities")

    for (identity in identities) {
        Logz.dspmsg("Identity: ${identity.name}")
        Logz.dspmsg("Encrypted: ${identity.isEncrypted}")

        val publicKey = identity.publicKeyBlob
        if (publicKey != null) {
            Logz.dspmsg("Public key blob length: ${publicKey.size}")
            Logz.dspmsg("JSch fingerprint: ${calculateKeyFingerprint(Path.of(pkpcp))}")
        }
    }

    Logz.dspmsg("privateKeyPath: ${pkf.canonicalPath}")

    val session = jsch.getSession(rhd.u, rhd.i, 22)


    try {
        session.connect(30000)
        val channelSftp = session.openChannel("sftp") as ChannelSftp
        return SshClient(session, channelSftp)
    } catch (e: Exception) {
        Logz.errorMsg("Failed to connect to $rhd")
        Logz.errorMsg("Check the remote logs using the command: ")
        Logz.errorMsg("sudo tail -f /var/log/auth.log")
        throw e
    }
}

fun calculateKeyFingerprint(privateKeyPath: Path): String {
    val jsch = JSch()

    val keyPair = KeyPair.load(
        jsch,
        privateKeyPath.toString()
    )

    try {
        return "Key type: ${keyPair.keyType}\n" +
                "JSch fingerprint: ${keyPair.fingerPrint}"
    } catch (e: Exception) {
        Logz.errorEx(e)
        return "Error accessing key pair for $privateKeyPath: ${e.message}"
    } finally {
        keyPair.dispose()
    }
}
