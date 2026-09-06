@echo off
set ERRORLEVEL=
call clean.cmd
call build.cmd
echo ERRORLEVEL is %ERRORLEVEL% before jpk.cmd
if [%ERRORLEVEL%] neq [0] goto end
call jpk.cmd
:end
