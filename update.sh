#!/bin/bash
if [ $# -eq 0 ]; then
 echo '*******************************************************************'
 echo '*** Exiting, no parameters were passed.                         ***'
 echo '*** Usage:                                                      ***'
 echo '*** ./update.sh "Git commit message"                            ***'
 echo '*******************************************************************'
 exit 1
fi
echo '*******************************************************************'
echo '*** UPDATING GIT REPOSITORY ON THE CURRENT BRANCH.              ***'
echo '*******************************************************************'
git pull
git add *
git commit -m "${1}"
git push
echo 'Git update complete.'
echo 'Updated git with the message:'
echo $1
echo '*******************************************************************'
echo '*** COMPLETED.                                                  ***'
echo '*******************************************************************'
