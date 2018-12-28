@echo off

set BUILD_HOME=..\
set ANT_HOME=..\tools\apache-ant
set SVN_HOME=..\tools\svn

set JAVA_OPTS=-Xmx256m -Xms256m -XX:MaxNewSize=64m -XX:NewSize=64m
set ANT_OPTS=%JAVA_OPTS%