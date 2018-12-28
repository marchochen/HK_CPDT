echo on
set JAVA_OPTIONS=-Xms48m -Xmx48m -XX:NewSize=16m -XX:MaxNewSize=16m
set LIB=..\..\..\www\WEB-INF\lib
set DRIVER_LIB=..\..\..\jdbc
set CONFIG=..\config
set APPNPATH=..\..\..\
set CLASSPATH=.;%LIB%\jaxb-api.jar;%LIB%\jaxb-ri.jar;%LIB%\jaxb-libs.jar;%LIB%\xml-apis.jar;%LIB%\xalan_wizbank.jar;%LIB%\xercesImpl.jar;%DRIVER_LIB%\ojdbc14.jar;%DRIVER_LIB%\jtds.jar;%DRIVER_LIB%\db2jcc.jar;%DRIVER_LIB%\db2jcc_license_cisuz.jar;%DRIVER_LIB%\db2jcc_license_cu.jar;%LIB%\PerlTools.jar;%LIB%\mail.jar;%LIB%\sunjce_provider.jar;%LIB%\wizbank.jar;%LIB%\enterprise.jar;%LIB%\configSchemaJaxb.jar 

java -showversion -hotspot %JAVA_OPTIONS% -cp %CLASSPATH% com.cw.wizbank.enterprise.IMSEnterpriseApp -inifile %CONFIG%\wizb.ini -configpath %APPNPATH% %1 %2 %3 %4 %5 %6 %7 %8 %9
