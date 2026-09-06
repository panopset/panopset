#!/bin/bash
if [ $# -eq 0 ]; then
 echo '*******************************************************************'
 echo '*** Exiting, no parameters were passed.                         ***'
 echo '*** Usage:                                                      ***'
 echo '*** ./all.sh "Git commit message"                               ***'
 echo '*******************************************************************'
 exit 1
fi
./all000.sh $1

