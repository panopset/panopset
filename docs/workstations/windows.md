[home](../../README.md) ~ [stack](../stack.md) ~ workstations ~ [setup](../setup/README.md)

[home](README.md) ~ [deb](deb.md) ~ [rpm](rpm.md) ~ [CachyOS](cachyos.md) ~ [mac](mac.md) ~ windows

On Windows systems, I like to keep a setup script in my home directory, which looks like this, but make adjustments
for your own system:

    set APPS=C:\apps
    set JAVA_HOME=%APPS%\jdks\jdk25
    set WIX_HOME=%APPS%\jdks\wix314-binaries
    set GRADLE_HOME=%APPS%\gradle
    set MAVEN_HOME=%APPS%\mvn
    set PATH=%PATH%^
    %JAVA_HOME%\bin;^
    %WIX_HOME%;^
    %GRADLE_HOME%\bin;^
    %MAVEN_HOME%\bin;
    set PAN_NM="Your Name"
    set PAN_EMAIL=you@company.com
    set PAN_WS_PW="foo"
    set PAN_SV_PW="bar"
    set PAN_REDIS_HOST="MyRedisHost.com"
    set PAN_REDIS_PORT=25061
    set PAN_REDIS_PWD="bat"

# Wix

To create a Windows .msi installer using jpackage (jpk.cmd), you'll need [Wix](https://docs.firegiant.com/wix/).

Make sure you are in compliance with the Wix End User License Agreement (EULA) before proceeding.

Once installed run:

    wix eula accept wix7
    wix extension add -g WixToolset.Util.wixext
    wix extension add -g WixToolset.UI.wixext
