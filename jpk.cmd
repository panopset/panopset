@echo off
if [%ERRORLEVEL%] neq [0] exit /b %ERRORLEVEL%
call checkenv.cmd
rmdir /s /q target
call readProps.cmd deploy.properties
call readProps.cmd appwin.properties
set R=%USERPROFILE%\.m2\repository
rem update ll.sh, llrpm.sh lm.sh, and shoring/%PAN%/pom.xml deploy.properties as well.
set MP=^
%R%/org/jetbrains/kotlin/kotlin-stdlib/%KV%/kotlin-stdlib-%KV%.jar;^
%R%/org/jetbrains/kotlin/kotlin-reflect/%KV%/kotlin-reflect-%KV%.jar;^
%R%/org/openjfx/javafx-base/%FXV%/javafx-base-%FXV%-%FX_ARCH%.jar;^
%R%/org/openjfx/javafx-graphics/%FXV%/javafx-graphics-%FXV%-%FX_ARCH%.jar;^
%R%/org/openjfx/javafx-controls/%FXV%/javafx-controls-%FXV%-%FX_ARCH%.jar;^
%R%/org/openjfx/javafx-web/%FXV%/javafx-web-%FXV%-%FX_ARCH%.jar;^
%R%/org/openjfx/javafx-media/%FXV%/javafx-media-%FXV%-%FX_ARCH%.jar;^
%R%/org/openjfx/jdk-jsobject/%FXV%/jdk-jsobject-%FXV%-%FX_ARCH%.jar;^
%R%/com/fasterxml/jackson/core/jackson-annotations/%JA%/jackson-annotations-%JA%.jar;^
%R%/tools/jackson/core/jackson-core/%JV%/jackson-core-%JV%.jar;^
%R%/tools/jackson/core/jackson-databind/%JV%/jackson-databind-%JV%.jar;^
%R%/com/github/mwiede/jsch/%SV%/jsch-%SV%.jar;^
%R%/com/panopset/compat/%PV%/compat-%PV%.jar;^
%R%/com/panopset/desk/%PV%/desk-%PV%.jar;^
%R%/com/panopset/flywheel/%PV%/flywheel-%PV%.jar;^
%R%/com/panopset/fxapp/%PV%/fxapp-%PV%.jar

set c=jpackage ^
  -n panopset ^
  -p %MP%;%JAVA_HOME%\jmods ^
  -m com.panopset.desk/com.panopset.compat.AppVersion ^
  --vendor "Panopset" ^
  --copyright "1996-2026 Karl Dinwiddie" ^
  --license-file LICENSE ^
  --description "Panopset desktop applications." ^
  --add-launcher version=launchers/version.properties ^
  --add-launcher dash=launchers/dash.properties ^
  --add-launcher gi=launchers/gi.properties ^
  --add-launcher gs=launchers/gs.properties ^
  --add-launcher gv=launchers/gv.properties ^
  --add-launcher fw=launchers/fw.properties ^
  --add-launcher flywheel=launchers/flywheel.properties ^
  --add-launcher checksum=launchers/checksum.properties ^
  --add-launcher scrambler=launchers/scrambler.properties ^
  --add-launcher lowerclass=launchers/lowerclass.properties ^
  --app-version %PV% ^
  --dest target/installer ^
  --win-dir-chooser ^
  --win-console ^
  --type msi ^
  --verbose

%c%

set c=jpackage ^
-n panopset ^
-p %MP%;%JAVA_HOME%\jmods ^
-m com.panopset.desk/com.panopset.compat.AppVersion ^
--vendor "Panopset" ^
--copyright "1996-2026 Karl Dinwiddie" ^
--description "Panopset desktop applications." ^
--add-launcher version=launchers/version.properties ^
--add-launcher dash=launchers/dash.properties ^
--add-launcher gi=launchers/gi.properties ^
--add-launcher gs=launchers/gs.properties ^
--add-launcher gv=launchers/gv.properties ^
--add-launcher fw=launchers/fw.properties ^
--add-launcher flywheel=launchers/flywheel.properties ^
--add-launcher checksum=launchers/checksum.properties ^
--add-launcher scrambler=launchers/scrambler.properties ^
--add-launcher lowerclass=launchers/lowerclass.properties ^
--app-version %PV% ^
--dest target/standalone ^
--win-console ^
--type app-image ^
--verbose

%c%

target\standalone\panopset\gi.exe appwin
