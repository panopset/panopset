[home](../../README.md) ~ [stack](../stack.md) ~ [workstations](../workstations/README.md) ~ setup

[home](README.md) ~ [env](env.md) ~ ssh ~ [qt](qt.md)

To set up ssh access to your server(s), you need 
[ssh keys](https://www.digitalocean.com/community/tutorials/how-to-configure-ssh-key-based-authentication-on-a-linux-server).*

Register your public key when you create your server, then you'll need to set up your ~/.ssh directory.

    cd
    mkdir .ssh
    chmod 700 .ssh
    cd .ssh

Add your public key to ~/.ssh/authorized_keys

Once you have set up your workstation as an ssh server, using its documentation, you can then easily
transfer your private key and the config over from an existing system,
into the ~/.ssh directory as well.

    chmod 400 (your private key)
    vim config

Add servers you want to ssh to:

    Host <server name>
    HostName <server IP address>
    User <your local workstation user ID>
    IdentityFile ~/.ssh/<private key file>

---

Next, you'll want to [stack](../stack.md) your components together.

---

*Panopset is hosted on [DigitalOcean](https://digitalocean.com) 
Ubuntu and ValKey servers, make any necessary adjustments for your setup.
