How to use the "Copy organization" batch programme

Usage:

1. backup the database of the site to be modified (in case any odd things happen)
2. open a new command prompt window
3. change the current directory to "<app root>\batch\organization\bin"
4. execute the script "copy_organization.cmd" with the following parameters:
a) ste_id of the source organization, e.g. "cw" (it is the ste_id of the organization you want to copy FROM)
b) ste_id of the target organization, e.g. "cw01" (it will the ste_id of the new organization you want to create. the use of this id is for internal data reference and for the folder name under "config\organization" only)
c) display name of the target organization, e.g. "Organization 01" (it will be used as the display name of that organization in the login page only. also, the drop down menu of organization in the login page is sorted by this name)
d) path to the directory of organization specific configuration files (e.g. "c:\wizbank\config\organization")
(if there is space in the parameter, do include double quotes in front of and after the parameter)
5. check in the database to see if a new record is inserted in table acSite. there should be a record that column "ste_id" contains the value you have specified in step 5.b) and column "ste_name" contains the value specified in step 5.c)
a) note down the ste_ent_id of this new record for the manual action below
6. execute the manual action described below

Note:

1. ste_id of an organization can be found in the column "ste_id" of the database table "acSite".
2. before the script is executed, make sure all necessary configurations are done in the source organization.
3. if the script is executed successfully, the following messages should be shown:
Finish copying Root Node.
Finish copying Recycle Bin.
Finish copying User Role.
Finish copying User Grade.
Finish copying Sys Users.
Finish copying Homepage Functions.
updating aeTemplate....
finished updating aeTemplate....
Finish copying Learning Soln Type.
Finish copying Management Report.
Finish copying aeAttendanceStatus.
Finish copying fmTimeSlot.
Finish copying fmFacilityType.
Finish copying Other Related Tables.
Finish copying Root Dirs and Files.
4. after the script is executed successfully, a new organization should be created:
a) in the directory of organization specific configuration files, there is a new directory with the same name as the ste_id of the target organization. and it will contain files copied from that of the source organization.
b) in the login page, there is a selection box labelled "Organization". and one of the selection is the display name of the target organization.


Manual action:

after a new organization is created, some manual actions have to be done:
1. modify the "import_role_list" parameter in the following configuration files:
a) <app root>\batch\enterprise\config\wizb.ini
b) <app root>\config\system\setupadv.xml
c) <app root>\www\config\wizb.ini
for example, if "import_role_list" has the value "NLRN_1~TADM_1" originally and the ste_ent_id you have got in step 6.a) above is 8, then "import_role_list" has to be changed to "NLRN_1~TADM_1~NLRN_8~TADM_8".

meaning of "import_role_list":
for users that are to be updated through "Data Import" or "IMS API", roles of a user are also updated. for these roles to be synchronized with the input data, external ID of these roles has to be specified in the parameter "import_role_list". if a role is not specified in this parameter, this role of a user will not be removed from the user. 
