set LIB=..\..\www\WEB-INF\lib
set DRIVER_LIB=..\..\jdbc
set APPNPATH=..\..\
java -showversion -cp %LIB%\wizbank.jar;%DRIVER_LIB%\jtds.jar;%DRIVER_LIB%\ojdbc14.jar;%DRIVER_LIB%\db2jcc.jar;%DRIVER_LIB%\db2jcc_license_cisuz.jar;%DRIVER_LIB%\db2jcc_license_cu.jar;%LIB%\jaxb-api.jar;%LIB%\jaxb-ri.jar;%LIB%\jaxb-libs.jar;%LIB%\PerlTools.jar;%LIB%\configSchemaJaxb.jar com.cw.wizbank.batch.bugfix.BugFix1741 %APPNPATH% 1>>bugfix1741_log.txt 2>&1