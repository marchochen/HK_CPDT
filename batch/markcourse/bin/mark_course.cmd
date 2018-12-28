echo on
set LIB=..\..\..\www\WEB-INF\lib
set DRIVER_LIB=..\..\..\jdbc
set LOG=..\log
set APPNPATH=..\..\..\
set CLASSPATH=%LIB%\wizbank.jar;%LIB%\configSchemaJaxb.jar;%LIB%\PerlTools.jar;%LIB%\jaxb-api.jar;%LIB%\jaxb-libs.jar;%LIB%\jaxb-ri.jar;%LIB%\sun_security_provider.jar;%LIB%\sunjce_provider.jar;%LIB%\xercesImpl.jar;%LIB%\xml-apis.jar;%DRIVER_LIB%\db2jcc.jar;%DRIVER_LIB%\db2jcc_license_cisuz.jar;%DRIVER_LIB%\db2jcc_license_cu.jar;%DRIVER_LIB%\jtds.jar;%DRIVER_LIB%\ojdbc14.jar

java -cp %CLASSPATH% com.cw.wizbank.batch.markEvaluation.MarkCourse -a -f ..\config\wizb.ini %APPNPATH% >> %LOG%\MarkEvaluationLog.txt
