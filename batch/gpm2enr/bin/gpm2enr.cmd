echo on
set java_home=C:\j2sdk1.4.2_16

set LIB=..\..\..\www\WEB-INF\lib
set DRIVER_LIB=..\..\..\jdbc
set LOG=..\log
set APPNPATH=..\..\..\
set CLASSPATH=%LIB%\wizbank.jar;%LIB%\configSchemaJaxb.jar;%LIB%\PerlTools.jar;%LIB%\jaxb-api.jar;%LIB%\jaxb-libs.jar;%LIB%\jaxb-ri.jar;%LIB%\sun_security_provider.jar;%LIB%\sunjce_provider.jar;%LIB%\xercesImpl.jar;%LIB%\xml-apis.jar;%DRIVER_LIB%\db2jcc.jar;%DRIVER_LIB%\db2jcc_license_cisuz.jar;%DRIVER_LIB%\db2jcc_license_cu.jar;%DRIVER_LIB%\jtds.jar;%DRIVER_LIB%\ojdbc14.jar

%java_home%\bin\java -cp %CLASSPATH% com.cw.wizbank.batch.gpm2enr.Gpm2Enr %APPNPATH% > %LOG%\Gpm2EnrLog.txt
