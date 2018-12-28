echo on
set LIB=..\..\..\www\WEB-INF\lib
set LOG=..\log
set CONFIGPATH=..\config
set FILEPATH=D:\temp
set UNICODEFILEIND=false
set CLASSPATH=%LIB%\wizbank.jar;%LIB%\jaxb-api.jar;%LIB%\jaxb-libs.jar;%LIB%\jaxb-ri.jar;%LIB%\dataloader.jar;%LIB%\PerlTools.jar;

java -cp %CLASSPATH% com.cw.wizbank.util.DataGenerator %CONFIGPATH% %FILEPATH% %UNICODEFILEIND% 1>>%LOG%\dataloader.txt 2>&1
