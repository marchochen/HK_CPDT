echo on
set JAVA_OPTIONS=-Xms256m -Xmx256m -XX:NewSize=64m -XX:MaxNewSize=64m
set LIB=..\..\..\www\WEB-INF\lib
set DRIVER_LIB=..\..\..\jdbc
set APPNPATH=..\..\..\
set CLASSPATH=.;%LIB%\xalan_wizbank.jar;%LIB%\xercesImpl.jar;%LIB%\xml-apis.jar;%DRIVER_LIB%\ojdbc14.jar;%DRIVER_LIB%\jtds.jar;%DRIVER_LIB%\db2jcc.jar;%DRIVER_LIB%\db2jcc_license_cisuz.jar;%DRIVER_LIB%\db2jcc_license_cu.jar;%LIB%\PerlTools.jar;%LIB%\mail.jar;%LIB%\wizbank.jar;%LIB%\configSchemaJaxb.jar;%LIB%\jaxb-api.jar;%LIB%\jaxb-ri.jar;%LIB%\jaxb-libs.jar;

java -showversion -hotspot %JAVA_OPTIONS% -cp %CLASSPATH% com.cw.wizbank.organization.cwOrgProducer %APPNPATH% %1 %2 %3 %4 %5 %6 %7 %8 %9
