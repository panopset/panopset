[home](../../README.md) ~ [stack](../stack.md) ~ [workstations](../workstations/README.md) ~ setup

[home](README.md) ~ [env](env.md) ~ brew ~ [ssh](ssh.md) ~ [qt](qt.md)

Install [HomeBrew](https://brew.sh/).


...then:

    brew install openjdk@25 maven

Periodically run this to make sure you have the latest of everything:

    brew update
    brew upgrade

Verify:

    mvn -version

... output should look something like this:

    Apache Maven 3.9.12 (848fbb4bf2d427b72bdb2471c22fced7ebd9a7a1)
    Maven home: /home/linuxbrew/.linuxbrew/Cellar/maven/3.9.12/libexec
    Java version: 25.0.1, vendor: Homebrew, runtime: /home/linuxbrew/.linuxbrew/Cellar/openjdk/25.0.1/libexec
    Default locale: en_US, platform encoding: UTF-8
    OS name: "linux", version: "6.14.0-37-generic", arch: "amd64", family: "unix"

---

Next thing to do is to make sure you can talk to your servers, by configuring [ssh](ssh.md).

