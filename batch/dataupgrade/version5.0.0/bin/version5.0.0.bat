@echo ON
set LIB=..\..\..\..\www\WEB-INF\lib
set DRIVER_LIB=..\..\..\..\jdbc
set APPNPATH=..\..\..\..\
set SERVLET_LIB=..\..\..\..\src\build\lib_compile

set LOG_FILE=..\log\log.txt
set CONFIG=..\config
set INIFILE=%config%\wizb.ini
set USER_PROPERTY_MAP=%config%\userpropertymap.txt
set CLASSPATH=%LIB%\sunjce_provider.jar;%DRIVER_LIB%\ojdbc14.jar;%LIB%\activation.jar;%DRIVER_LIB%\db2jcc.jar;%LIB%\jaxb-api.jar;%LIB%\jaxb-libs.jar;%LIB%\jaxb-ri.jar;%DRIVER_LIB%\jtds.jar;%LIB%\PerlTools.jar;%LIB%\xalan_wizbank.jar;%LIB%\xercesImpl.jar;%LIB%\xml-api.jar;%LIB%\wizbank.jar;%LIB%\configSchemaJaxb.jar;%LIB%\commons-beanutils.jar;%LIB%\json-lib-2.2-jdk13.jar;%SERVLET_LIB%\servlet.jar

@echo on
java -showversion -cp %CLASSPATH% com.cw.wizbank.dataupgrade.Version5_0_0 -inifile %INIFILE% -approot %APPNPATH% -userpropertymapfile %USER_PROPERTY_MAP% -log %LOG_FILE%