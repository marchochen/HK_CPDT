rem  This program will update CourseCriteria table, CourseModuleCriteria table and CourseMeasurement table.
rem     Insert one record into CourseCriteria table for every Class_base training, Web_base training and Class;
rem     Insert one record into CourseMeasurement table for every record in CourseModuleCriteria table;
rem     Update CourseModuleCriteria table:
rem  	if cmr_status = null set cmr_contri_rate = 0 and cmr_is_contri_by_score = 0;
rem  	if cmr_status != null give cmr_status_desc_option a corresponding number(0-7). 
rem  
rem  How to run
rem  ==========
rem  It takes in an ini file showing DB connection information.
rem  e.g. 
rem  >updateCMR wizb.ini
rem  
rem  Expected result
rem  ===============
rem  This program will print out the following messages:
rem  1) Message showing if it can make DB connection successfully;
rem  2) Message showing how many records are updated and some necessary information;
rem  3) Message showing connection is commited or rollbacked.


@echo off
call setenv.bat
set CLASSPATH=%LIB%\sunjce_provider.jar;%DRIVER_LIB%\ojdbc14.jar;%LIB%\activation.jar;%DRIVER_LIB%\db2jcc.jar;%LIB%\jaxb-api.jar;%LIB%\jaxb-libs.jar;%LIB%\jaxb-ri.jar;%DRIVER_LIB%\jtds.jar;%LIB%\PerlTools.jar;%LIB%\xalan_wizbank.jar;%LIB%\xercesImpl.jar;%LIB%\xml-api.jar;%LIB%\wizbank.jar;%LIB%\configSchemaJaxb.jar;

@echo on
java -showversion -cp %CLASSPATH% com.cw.wizbank.dataupgrade.updCMR.UpdateTool %APPNPATH%
