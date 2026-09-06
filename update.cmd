@echo off
if [%1]==[] goto usage
echo *******************************************************************
echo *** UPDATING GIT REPOSITORY ON THE CURRENT BRANCH.              ***
echo *******************************************************************
git pull
git add *
git commit -m %1
git push
echo Git update complete.
echo Updated git with the message:
echo [%1]
echo *******************************************************************
echo *** COMPLETED.                                                  ***
echo *******************************************************************
goto end
:usage
echo *******************************************************************
echo *** Exiting, no parameters were passed.                         ***
echo *** Usage:                                                      ***
echo *** update.cmd "Git commit message"                             ***
echo *******************************************************************
exit /B 1
:end
