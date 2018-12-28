@echo off
set LIB=..\..\..\www\WEB-INF\lib
set DRIVER_LIB=..\..\..\jdbc
set APPNPATH=..\..\..\
set CONFIG=..\config\wizb.ini
set CLASSPATH=%LIB%\sunjce_provider.jar;%DRIVER_LIB%\ojdbc14.jar;%LIB%\activation.jar;%DRIVER_LIB%\db2jcc.jar;%DRIVER_LIB%\db2jcc_license_cisuz.jar;%DRIVER_LIB%\db2jcc_license_cu.jar;%LIB%\jaxb-api.jar;%LIB%\jaxb-libs.jar;%LIB%\jaxb-ri.jar;%DRIVER_LIB%\jtds.jar;%LIB%\PerlTools.jar;%LIB%\xalan_wizbank.jar;%LIB%\xercesImpl.jar;%LIB%\xml-api.jar;%LIB%\wizbank.jar;%LIB%\configSchemaJaxb.jar;

@echo on
java -showversion -cp %CLASSPATH% com.cw.wizbank.batch.updateTrainingCenter.UpdateTc %APPNPATH% %CONFIG%
