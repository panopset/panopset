call checkenv.cmd
if %errorlevel% neq 0 exit /b %errorlevel%
git config --global user.name %PAN_NM%
git config --global user.email %PAN_EMAIL%
git config advice.addIgnoredFile false
