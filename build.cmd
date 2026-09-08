@echo off
call readProps.cmd deploy.properties
echo ERRORLEVEL is %ERRORLEVEL% at start of build.cmd.
if [%ERRORLEVEL%] neq [0] exit /b %ERRORLEVEL%
call checkenv.cmd
if [%ERRORLEVEL%] neq [0] exit /b %ERRORLEVEL%
echo *******************************************************************
echo *** Building %PAN%                                              ***
echo *******************************************************************
call mvn -f projects/shoring/%PAN%/ install
if [%ERRORLEVEL%] neq [0] exit /b %ERRORLEVEL%
echo *******************************************************************
echo *** Building beam                                               ***
echo *******************************************************************
call mvn -f projects/beam/ install
echo Build complete.
