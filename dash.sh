#!/bin/bash
if [[ $OSTYPE == "darwin"* ]]; then
  target/standalone/panopset.app/Contents/MacOS/dash appmac
else
  RPM="$(which rpm)"
  if [[ $RPM == "/usr/bin/rpm" ]]
  then
    target/standalone/panopset/bin/dash applinuxrpm
  else
    target/standalone/panopset/bin/dash applinuxdeb
  fi
fi
