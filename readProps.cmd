@echo off

rem Modified from https://stackoverflow.com/questions/7708681.

for /F "eol=# delims== tokens=1,*" %%a in (%1%) do (
 if NOT "%%a"=="" if NOT "%%b"=="" set %%a=%%b
)
