#!/bin/bash
if [[ $OSTYPE == "darwin"* ]]; then
  target/standalone/panopset.app/Contents/MacOS/dash mac
else
  RPM="$(which rpm)"
  if [[ $RPM == "/usr/bin/rpm" ]]
  then
    target/standalone/panopset/bin/dash linuxrpm
  else
    target/standalone/panopset/bin/dash linuxdeb
  fi
fi
