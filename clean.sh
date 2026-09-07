#!/bin/bash
source deploy.properties
mvn -f projects/shoring/$PAN clean
mvn -f projects/beam/ clean
