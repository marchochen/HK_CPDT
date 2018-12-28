@echo off
call setenv.bat
set LOG_FILE=..\log\log.txt
set RESOURSE_FOLDER=..\..\..\..\www\resource
set CONFIG=..\config\wizb.ini
set CLASSPATH=%LIB%\sunjce_provider.jar;%DRIVER_LIB%\ojdbc14.jar;%LIB%\activation.jar;%DRIVER_LIB%\db2jcc.jar;%LIB%\jaxb-api.jar;%LIB%\jaxb-libs.jar;%LIB%\jaxb-ri.jar;%DRIVER_LIB%\jtds.jar;%LIB%\PerlTools.jar;%LIB%\xalan_wizbank.jar;%LIB%\xercesImpl.jar;%LIB%\xml-api.jar;%LIB%\wizbank.jar;%LIB%\configSchemaJaxb.jar;

@echo on
java -showversion -cp %CLASSPATH% com.cw.wizbank.dataupgrade.disMod2Cls.DisMod2Cls %CONFIG% %APPNPATH% %LOG_FILE% %RESOURSE_FOLDER%
