[home](../../README.md) ~ [stack](../stack.md) ~ workstations ~ [setup](../setup/README.md)

[home](README.md) ~ [deb](deb.md) ~ rpm ~ [CachyOS](cachyos.md) ~ [mac](mac.md) ~ [windows](windows.md)

RPM Linux (Fedora/RedHat)


Redhat Package Manager (RPM) systems use yum to install things:

    sudo yum install git gitk vim rpm-build

To determine what the JAVA_HOME environment variable should be: 

    readlink -f /etc/alternatives/java

use output to set JAVA_HOME in /etc/profile.d/java.sh, something like:

    export JAVA_HOME=/usr/lib/jvm/java-25-openjdk

We also need the jmods:

    sudo dnf install java-25-openjdk-jmods

