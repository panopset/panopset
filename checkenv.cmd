@echo off
call readProps.cmd deploy.properties
echo Checking the environment variables.
if [%PANREQDEF%] == [T] GOTO end
echo Checking JAVA_HOME
if [%JAVA_HOME%] == [] GOTO LabelFail
echo Checking PAN_NM
if [%PAN_NM%] == [] GOTO LabelFail
echo Hello %PAN_NM%
echo Checking PAN_EMAIL
if [%PAN_EMAIL%] == [] GOTO LabelFail
echo Checking PAN_WS_PW
if [%PAN_WS_PW%] == [] GOTO LabelFail
echo Checking PAN_SV_PW
if [%PAN_SV_PW%] == [] GOTO LabelFail
echo Checking PAN_REDIS_HOST
if [%PAN_REDIS_HOST%] == [] GOTO LabelFail
echo Checking PAN_REDIS_PORT
if [%PAN_REDIS_PORT%] == [] GOTO LabelFail
echo Checking PAN_REDIS_PWD
if [%PAN_REDIS_PWD%] == [] GOTO LabelFail
echo Checking PAN, if this check fails make sure deploy.properties is valid and loaded.
if [%PAN%] == [] GOTO LabelFail
echo All environment variables are there.
GOTO good

:LabelFail

echo ******************************************************************
echo ***                                                            ***
echo *** checkenv failed.                                           ***
echo ***                                                            ***
echo ***   please make sure all environment variables are defined.  ***
echo ***   see docs/setup/env.md                                    ***
echo ***                                                            ***
echo ******************************************************************
exit /b 1

:good

echo All required environment variables are defined, setting PANREQDEF to T to skip checkenv for this session.
set PANREQDEF=T

:end
