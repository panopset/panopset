#!/bin/bash
source deploy.properties
mvn -f projects/shoring/$PAN clean install
mvn -f projects/beam/ clean install
