@echo off

set LIB=..\lib
set CLASSPATH=..\..\..\www\WEB-INF\classes;..\..\..\www\WEB-INF\lib\wizbank.jar;%LIB%\sunjce_provider.jar;%LIB%\US_export_policy.jar;%LIB%\jce1_2_2.jar;%LIB%\local_policy.jar;


@echo on
C:\jdk1.5\bin\java.exe -showversion -cp %CLASSPATH% com.cw.wizbank.batch.encrypt.EncryptUtil MAX_ACCOUNT
