#!/bin/bash
if [[ -z "${JAVA_HOME}" ]]; then
echo "JAVA_HOME is not defined, exiting."
exit 1
fi
echo "Packaging apps for ${OSTYPE}..."
source deploy.properties

if [[ -z "${PV}" ]]; then
echo "Exiting because PV is undefined"
exit 1
fi

if [[ $OSTYPE == "darwin"* ]]; then
  echo "*** MAC platform ***"
  source appmac.properties
else
  RPM="$(which rpm)"
  echo $RPM
  if [[ $RPM == "/usr/bin/rpm" ]]
  then
    echo "*** RPM platform ***"
    source applinuxrpm.properties
  else
    echo "*** DEB platform ***"
    source applinuxdeb.properties
  fi
fi

export R=$HOME/.m2/repository
echo "PLATFORM_KEY is ${PLATFORM_KEY}"
echo "PLATFORM_NAME is ${PLATFORM_NAME}"
echo "Repository base is $R "
echo "Panopset version (PV) is $PV"

export MP=\
$R/org/jetbrains/kotlin/kotlin-stdlib/$KV/kotlin-stdlib-$KV.jar:\
$R/org/jetbrains/kotlin/kotlin-reflect/$KV/kotlin-reflect-$KV.jar:\
$R/org/openjfx/javafx-base/$FXV/javafx-base-$FXV-$FX_ARCH.jar:\
$R/org/openjfx/javafx-graphics/$FXV/javafx-graphics-$FXV-$FX_ARCH.jar:\
$R/org/openjfx/javafx-controls/$FXV/javafx-controls-$FXV-$FX_ARCH.jar:\
$R/org/openjfx/javafx-web/$FXV/javafx-web-$FXV-$FX_ARCH.jar:\
$R/org/openjfx/javafx-media/$FXV/javafx-media-$FXV-$FX_ARCH.jar:\
$R/org/openjfx/jdk-jsobject/$FXV/jdk-jsobject-$FXV-$FX_ARCH.jar:\
$R/com/fasterxml/jackson/core/jackson-annotations/$JA/jackson-annotations-$JA.jar:\
$R/tools/jackson/core/jackson-core/$JV/jackson-core-$JV.jar:\
$R/tools/jackson/core/jackson-databind/$JV/jackson-databind-$JV.jar:\
$R/com/github/mwiede/jsch/$SV/jsch-$SV.jar:\
$R/com/panopset/compat/$PV/compat-$PV.jar:\
$R/com/panopset/desk/$PV/desk-$PV.jar:\
$R/com/panopset/flywheel/$PV/flywheel-$PV.jar:\
$R/com/panopset/fxapp/$PV/fxapp-$PV.jar

rm -rf target

export c="jpackage \
  -n panopset \
  -p ${MP}:${JAVA_HOME}/jmods \
  -m com.panopset.desk/com.panopset.compat.AppVersion \
  --vendor 'Panopset' \
  --copyright '1996-2026 Karl Dinwiddie' \
  --license-file LICENSE \
  --description 'Panopset desktop applications.' \
  --add-launcher version=launchers/version.properties \
  --add-launcher dash=launchers/dash.properties \
  --add-launcher gi=launchers/gi.properties \
  --add-launcher gs=launchers/gs.properties \
  --add-launcher gv=launchers/gv.properties \
  --add-launcher fw=launchers/fw.properties \
  --add-launcher flywheel=launchers/flywheel.properties \
  --add-launcher checksum=launchers/checksum.properties \
  --add-launcher scrambler=launchers/scrambler.properties \
  --add-launcher lowerclass=launchers/lowerclass.properties \
  --app-version $PV \
  --dest target/installer \
  $INSTALLER_XTR"
echo $c
eval $c

export c="jpackage \
  -n panopset \
  -p ${MP}:${JAVA_HOME}/jmods \
  -m com.panopset.desk/com.panopset.compat.AppVersion \
  --vendor 'Panopset' \
  --copyright '1996-2026 Karl Dinwiddie' \
  --description 'Panopset desktop applications.' \
  --add-launcher version=launchers/version.properties \
  --add-launcher dash=launchers/dash.properties \
  --add-launcher gi=launchers/gi.properties \
  --add-launcher gs=launchers/gs.properties \
  --add-launcher gv=launchers/gv.properties \
  --add-launcher fw=launchers/fw.properties \
  --add-launcher flywheel=launchers/flywheel.properties \
  --add-launcher checksum=launchers/checksum.properties \
  --add-launcher scrambler=launchers/scrambler.properties \
  --add-launcher lowerclass=launchers/lowerclass.properties \
  --app-version $PV \
  --dest target/standalone \
  --type app-image"
eval $c

if [[ $OSTYPE == "darwin"* ]]; then
  echo "*** Creating json for MAC platform ***"
  target/standalone/panopset.app/Contents/MacOS/gi appmac
else
  RPM="$(which rpm)"
  echo $RPM
  if [[ $RPM == "/usr/bin/rpm" ]]
  then
    echo "*** Creating json for RPM platform ***"
    target/standalone/panopset/bin/gi applinuxrpm
  else
    echo "*** Creating json for DEB platform ***"
    target/standalone/panopset/bin/gi applinuxdeb
  fi
fi
