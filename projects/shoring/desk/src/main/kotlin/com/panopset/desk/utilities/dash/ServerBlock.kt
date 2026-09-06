package com.panopset.desk.utilities.dash

fun genInitServerBlockText(d: String): String {
    val serverBlockText =  "server {\n" +
            " listen 80;\n" +
            " listen [::]:80;\n" +
            " root /var/www/$d/html;\n" +
            " index index.html index.htm index.nginx-debian.html;\n" +
            " server_name $d www.$d;\n" +
            " location / {\n" +
            $$"  try_files $uri $uri/ =404;\n" +
            " }\n}\n" +
            genServerBlockLink(d) + createCertbotCommandString(d)
    return serverBlockText
}

fun createCertbotCommandString(d: String): String {
    return "sudo certbot --nginx -d $d -d www.$d"
}

fun genServerBlockLink(d: String): String {
    return "\nTo link your server block:" +
            "\nsudo ln -s /etc/nginx/sites-available/$d /etc/nginx/sites-enabled/\n\n"
}
