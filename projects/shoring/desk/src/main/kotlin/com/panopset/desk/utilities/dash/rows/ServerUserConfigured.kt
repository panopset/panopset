package com.panopset.desk.utilities.dash.rows

import com.panopset.compat.Fileop
import com.panopset.compat.HttpResponsePackage
import com.panopset.compat.RemoteHostData
import com.panopset.compat.Stringop
import com.panopset.compat.doGetHttp
import com.panopset.compat.opSecureCopyGet
import com.panopset.desk.utilities.dash.GrandCentralStation
import com.panopset.desk.utilities.dash.createCertbotCommandString
import com.panopset.desk.utilities.dash.genInitServerBlockText
import com.panopset.desk.utilities.dash.sm.ServerState
import java.io.File
import java.nio.file.Files

class ServerUserConfigured(gcs: GrandCentralStation): Report(gcs) {
    override fun updateOutput() {
        val rhd = gcs.createRemoteHostData()
        val u = Stringop.USN
        val h = rhd.h
        val d = rhd.d
        val i = rhd.i
        val tmpFile = File("./tmp")
        val nginxFile = File("$tmpFile/$d")
        tmpFile.deleteRecursively()
        Files.createDirectories(tmpFile.toPath())
        val fromScp = "/etc/nginx/sites-available/$d"
        val toScp = tmpFile.toPath().toString()
        try {
            opSecureCopyGet(rhd, fromScp, toScp)
        } catch (e: Exception) {
            val unableToScpMsg = "Unable to scp $fromScp $toScp\n"
            val pleaseTryCommandLine = "Please try from the command line and troubleshoot from there.\n"
            val otherIdeas = "If the server has been swapped, you need to delete ~/.ssh/known_hosts.\n" +
                    "Also, if it is a brand new server, make sure the User is root.\n"
            if (e.message == null) {
                gcs.server.setStateWithMessage(ServerState.Invalid,
                    unableToScpMsg + pleaseTryCommandLine + otherIdeas)
                return
            }
            val errorMsg = e.message!!.lowercase()
            if (errorMsg.indexOf("file") > -1) {
                showStateReport(createServerBlockInfo(fromScp, rhd))
                return
            } else if (errorMsg.indexOf("hostkey") > -1) {
                gcs.server.setStateWithMessage(ServerState.Invalid,
                    unableToScpMsg +
                            "due to ${e.message}\n" +
                            "It looks like you need to:\n    rm ~/.ssh/known_hosts\nssh $h\n\n" +
                            "... and acknowledge the new HostKey from the command line,\n" +
                            "before trying it from Panopset dash.")
                return
            } else {
                gcs.server.setStateWithMessage(ServerState.Invalid,
                    unableToScpMsg +
                    "due to ${e.message}\n" + pleaseTryCommandLine + otherIdeas)
                return
            }
        }
        var result: String
        if (nginxFile.exists()) {
            result = "nginx configured on $d at host $h, however SSL not set up yet.\n\n" +
            createCertbotCommandString(d) + "\n\n" + createFirstHtmlFileInstructions()
            for (l in Fileop.readLines(nginxFile)) {
                if (l.indexOf("ssl") > 1) {
                    val hrps: HttpResponsePackage = doGetHttp("https://${rhd.d}")

                    when (hrps.responseCode) {
                        200 -> {
                            gcs.server.state = ServerState.LetsEncryptDone
                            return
                        }
                        301 -> {
                            result = "Shouldn't get a 301 from an https request."
                        }
                        -1 -> {
                            result = "$d is not responding, check logs\n. " +
                                    "sudo cat /var/log/nginx/error.log\n" +
                                    "Verify your DNS settings:\n\n" +
                                    "www.$d CNAME $d\n@ A $i\n$d A $i\n* A $i\n\n" +
                                    "Verify ping $d comes back with $i\n\n" +
                                    "Verify your ~/.ssh/config file entry for this server looks like:\n" +
                                    "Host $h\nHostName $i\nUser <root or $u>\nIdentityFile ~/.ssh/<your public key file>\n\n" +
                                    "Verify nginx is installed, up and running.\n" +
                                    createFirstHtmlFileInstructions()

                        }
                        else -> {
                            result = "SSL configured and ready on $d at host $h, however https://${rhd.d} responded with:\n" +
                            "${hrps.responseCode}: ${hrps.errorMessage}"
                        }
                    }

                }
            }
        } else {
            result = "Server block file /etc/nginx/sites-available/$d not found. \n" +
                    createServerBlockInfo(fromScp, rhd)
        }
        showStateReport(result)
    }

    private fun createServerBlockInfo(fromScp: String, rhd: RemoteHostData): String {
        val h = rhd.h
        val d = rhd.d
        val u = rhd.u
        return "Next step is to set up your initial server block.\n\nssh $h\n" +
                "sudo vim $fromScp\n\n" +
                genInitServerBlockText(d) + "\n" +
                "sudo mkdir -p /var/www/$d/html\n" +
                "sudo chown -R $u:$u /var/www/$d/html\n" +
                "sudo chmod -R 755 /var/www/$d\n\n" +
                REFRESH_PROMPT +
                "$doc0\n$doc1"
    }
}

private const val doc0 = "\nhttps://www.digitalocean.com/community/tutorials/how-to-install-nginx-on-ubuntu-20-04"
private const val doc1 = "\nhttps://www.digitalocean.com/community/tutorials/how-to-secure-nginx-with-let-s-encrypt-on-ubuntu-20-04"
