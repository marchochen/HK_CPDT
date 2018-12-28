@echo off

call setenv.bat

:checkAnt
rem find ANT_HOME if it does not exist 
if exist "%ANT_HOME%\lib\ant.jar" goto checkSvn

:noAntHome
echo Build failed. ANT_HOME is set incorrectly or ant could not be located. Please set ANT_HOME.
goto end

:checkSvn
rem find SVN_HOME if it does not exist 
if exist "%SVN_HOME%\bin\svn.exe" goto setCMD

:noSvnHome
echo Build failed. SVN_HOME is set incorrectly or ant could not be located. Please set SVN_HOME.
goto end

:setCMD
set ANTCMD=%ANT_HOME%/bin/ant.bat
set SVNCMD=%SVN_HOME%/bin/svn.exe

:kickOff
call %ANTCMD% -f ..\config\build.xml

:end
