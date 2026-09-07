package com.panopset.desk.utilities.dash.rows

import com.panopset.compat.Stringop
import com.panopset.desk.utilities.dash.CONFIG_HOST_INDEX
import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.desk.utilities.dash.SshConfig

class ServerBrandNew(gcs: GrandCentralStation): Report(gcs) {
    override fun updateOutput() {
        val rhd = gcs.createRemoteHostData()
        val i = rhd.i
        val u = Stringop.USN
        val configValues = SshConfig.getConfigForIP(i)
        val h = configValues[CONFIG_HOST_INDEX]
        val report = "Host defined in ~/.ssh/config is: $h\n\nFull config is:\n\n" +
                getConfigString(configValues) + "\n\n" +
                "IMPORTANT: Make sure you delete known_hosts if you swapped servers under a reserved IP:\n\n" +
                "rm ~/.ssh/known_hosts\n\n" +
                "Your next step is to create a new user, and add necessary software.\n\n" +
                "ssh $h\n" +
                "apt update\n" +
                "apt -y upgrade\n" +
                "apt -y autoremove\n" +
                "sudo reboot 0\n" +
                "(Take a short stretch break)\n" +
                "ssh $h\n" +
                "apt -y install nginx net-tools certbot python3-certbot-nginx openjdk-25-jre-headless\n" +
                "ufw allow OpenSSH\n" +
                "ufw allow 'Nginx Full'\n" +
                "ufw app list\n" +
                "ufw enable\n" +
                "ufw status\n" +
                "adduser $u\n" +
                "usermod -aG sudo $u\n" +
                "rsync --archive --chown=$u:$u ~/.ssh /home/$u\n" +
                "Once that is done, update your ~/.ssh/config file so that the $h User is $u, \n" +
                "Make sure you can ssh $h as $u from another terminal,\n" +
                REFRESH_PROMPT +
                "https://www.digitalocean.com/community/tutorials/initial-server-setup-with-ubuntu-20-04"
        showStateReport(report)
    }

}

private fun getConfigString(configValues: List<String>): String {
    if (configValues.size == 4) {
        return "Host=${configValues[0]}\n" +
                "HostName=${configValues[1]}\n" +
                "User=${configValues[2]}\n" +
                "IdentityFile=${configValues[3]}\n"
    }
    return ""
}
