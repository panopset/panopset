[home](../../README.md) ~ [stack](../stack.md) ~ workstations ~ [setup](../setup/README.md)

[home](README.md) ~ deb ~ [rpm](rpm.md) ~ [CachyOS](cachyos.md) ~ [mac](mac.md) ~ [windows](windows.md)

# DEB Linux (Mint/Ubuntu)

Linux [Mint](https://linuxmint.com/) is the primary Panopset developer workstation OS.

Desktop software is assembled on their respective platforms, final site publishing is done on Linux Mint.

For Debian (DEB) based systems, we start with installing git, vim (optional, you may use any text editor), and build-essential:

    ./setup.sh

build-essential comes with Linux Mint these days, but I include it in the docs anyway, in case your DEB based [distro](https://distrowatch.com/) doesn't include it.

Also added some [qt](../setup/qt.md) dependencies, not sure yet if we stay with JavaFX, or go with Flutter or Qt for desktop applications.

Once you have a workstation set up, next you'll want to create some environment variables*.

On Linux MINT, you would add them to the end of your .profile file.

    vim ~/.profile

... or you could have a standalone script like we do on the Windows platform.

*Probably best to continue with [env](../setup/env.md).
