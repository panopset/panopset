package com.panopset.compat

import com.jcraft.jsch.Logger

class JSchLogger: Logger {
    override fun isEnabled(level: Int): Boolean = true

    override fun log(level: Int, message: String?) {
        println("JSch[$level]: $message")
//        println("Use this command to see how the server looks:")
//        println(" ssh -vvv -i ~/.ssh/<private key> <user>@<host>")
//        println("Look for signing using rsa-sha2-512.")
// println("On the server side, see if you see anything in here:
    // sudo tail -100 /var/log/auth.log

    }
}