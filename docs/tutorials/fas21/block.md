[home](README.md)

# Block

Just as nobody builds a house without a blueprint,
you shouldn't build a website without being able to
go back and easily reproduce each step.

In the 
[instructions](https://www.digitalocean.com/community/tutorials/how-to-install-nginx-on-ubuntu-20-04)
for setting up server blocks, we see that we need to execute
some commands, on the server, such as:

    sudo mkdir -p /var/www/your_domain/html

One option is to ssh out to your server and execute the commands, but
that leaves you in a lurch, if your site ever has to be re-built.

We need to

* Create and document some scripts.
* Optionally abstract the script, using parameters obtained from a bootstrap file.
  * Added SITE_FAS21 to ~/Documents/panopset/dev.properties.
* Structure the scripts so that they may be run all at once or pieces at a time.
* Execute it.

The deploy.sh script assumes an existing server which needs a new server block.

If you are doing this on a brand new server, see the 
[Panopset Website Deployment Manual](../../deploy.md).

(TODO: Abstract out the initial server setup steps.)

Dev system:

    cd docs/tutorials/fas21/scripts
    ./deploy.sh



