@echo off
rmdir /s /q target
call readProps.cmd deploy.properties
call mvn -f projects/shoring/%PAN% clean
call mvn -f projects/beam clean
