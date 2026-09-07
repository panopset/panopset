package com.panopset.desk.utilities.dash.rows

import com.panopset.desk.utilities.dash.CONFIG_HOST_NAME_INDEX
import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.desk.utilities.dash.SshConfig
import com.panopset.desk.utilities.dash.sm.ServerState

class ServerUnknown(gcs: GrandCentralStation): Report(gcs) {
    override fun updateOutput() {
        if (gcs.projectDirectorySelector.inputFile.text.isEmpty()) {
            gcs.server.setStateWithMessage(ServerState.Invalid,
                "Please select a valid project directory, by clicking the Project button above."
            )
            return
        }
        gcs.projectDir = gcs.projectDirectorySelector.createDir()
        if (!(gcs.projectDir.exists())) {
            gcs.server.setStateWithMessage(ServerState.Invalid,
                "Directory ${gcs.projectDir.canonicalPath} does not exist."
            )
            return
        }
        if (!(gcs.projectDir.isDirectory())) {
            gcs.server.setStateWithMessage(ServerState.Invalid,
                "${gcs.projectDir.canonicalPath} is not a directory."
            )
            return
        }
        if (gcs.osInfoMap.isEmpty()) {
            gcs.server.setStateWithMessage(ServerState.Invalid,
                "OS Info map failed to load, check that Dash launch parameter matches a" +
                        " valid and populated os properties file name."
            )
            return
        }
        val ip = gcs.ipTF.text
        if (ip.isBlank()) {
            gcs.server.setStateWithMessage(ServerState.Invalid,
                "Enter an IP address to load ssh config details" +
                        "in the \"IP address\" field upper right above.\n" +
                        "It will be one of the HostName entries:\n\n" +
                        domainConfigInfo())
            return
        }
        val domainName = gcs.domainTF.text
        if (domainName.isBlank()) {
            gcs.server.setStateWithMessage(ServerState.Invalid,
                "Please enter the Domain Name you have chosen for $ip\n" +
                        "in the website field upper left above.\n" +
                        REFRESH_PROMPT)
            return
        }
        val rhd = gcs.createRemoteHostData()
        if (rhd.h.isBlank()) {
            gcs.server.setStateWithMessage(ServerState.Invalid,
                "Host not defined in ~/.ssh/config for ${gcs.ipTF.text}.\n" +
                        domainConfigInfo())
            return
        }
        if (SshConfig.getConfigForIP(ip).isEmpty()) {
            gcs.server.setStateWithMessage(ServerState.Invalid,
                "No .ssh/config value found for $ip.\n" +
                        domainConfigInfo())
            return
        }
        if (rhd.u.isBlank()) {
            gcs.server.setStateWithMessage(ServerState.Invalid,
                "User not defined in ~/.ssh/config for ${rhd.h}")
            return
        }
        val configValues = SshConfig.getConfigForIP(ip)
        if (configValues.isEmpty()) {
            gcs.server.setStateWithMessage(ServerState.Invalid,
                "No .ssh/config value found for $ip.\n" +
                    "Please double check that $ip matches the HostName value in ~/.ssh/config.\n\n" +
                    domainConfigInfo())
            return
        }
        if (configValues[CONFIG_HOST_NAME_INDEX] != ip) {
            gcs.server.setStateWithMessage(ServerState.Invalid,
                "No matching .ssh/config value found for $ip.\n" +
                        "Please double check that $ip matches the HostName value in ~/.ssh/config.\n\n" +
                        domainConfigInfo())
            return
        }
        gcs.server.message = ""
        if (rhd.u == "root") {
            gcs.server.state = ServerState.BrandNew
        } else {
            gcs.server.state = ServerState.UserConfigured
        }
    }

    private fun domainConfigInfo(): String {
        return "vim ~/.ssh/config\n\n" +
               "Host <Your label for the host>\n" +
               "HostName <Your domain IP address>\n" +
               "User root\n" +
               "IdentityFile ~/.ssh/<Your private key>\n\n" +
                "Correct " + REFRESH_PROMPT + "\n\n" +
               "https://www.digitalocean.com/community/tutorials/how-to-use-ssh-to-connect-to-a-remote-server"
    }
}
