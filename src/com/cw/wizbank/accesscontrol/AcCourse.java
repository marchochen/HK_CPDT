package com.cw.wizbank.accesscontrol;

import java.sql.*;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbResourcePermission;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.security.AclFunction;

/**
Logical Layer Access Control on Course<BR>
UNDER CONSTRUCTION
*/
public class AcCourse extends AcResources {

    public AcCourse(Connection con) {
        super(con);
    }

    /**
    function ext id, course content management
    *//*
    public static String FTN_COS_CONTENT_MGT = "COS_CONTENT_MGT";


    *//**
    function ext id, offline course read
    *//*
    public static final String FTN_COS_OFF_READ = "COS_OFF_READ";

    *//**
    function ext id, course report read
    *//*
    public static final String FTN_COS_RPT_READ = "COS_RPT_READ";

    *//**
    function manage in the organsation
    *//*
    public static final String FTN_COS_MGT_IN_ORG = AclFunction.FTN_TEMP;

    *//**
    function ext id, read in ResourcePermission
    *//*
    public static final String FTN_COS_PERMISSION_READ = "ITM_COS_MAIN";

    *//**
    function ext id, write in ResourcePermission
    *//*
    public static final String FTN_COS_PERMISSION_WRITE = "ITM_COS_MAIN";

    *//**
    function ext id, execute in ResourcePermission
    *//*
    public static final String FTN_COS_PERMISSION_EXEC = "ITM_COS_MAIN";

    *//**
    check if the user, role pair has the privilege to read offline course
    */
    public boolean hasOffReadPrivilege(long ent_id, String rol_ext_id) throws SQLException {
        
    	String ftns[]= new String [] {AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN,
                AclFunction.FTN_AMD_ITM_COS_MAIN_CONTENT,AclFunction.FTN_AMD_EXAM_MAIN_PERFORMANCE};
    	return AccessControlWZB.hasRolePrivilege( rol_ext_id, ftns);
    }

   
   
   
   
   
   // Check wether a user has right to modify a course according to ACL
   // Case 1 : User has write permission in the resource permission
   // Case 2 : User has COS_MGT_IN_ORG right in ACL
   public boolean checkModifyPermission(loginProfile prof, long res_id) 
        throws qdbException, SQLException, cwSysMessage
   {
           
            String ftns[]= new String [] {AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN,
            		                      AclFunction.FTN_AMD_ITM_COS_MAIN_CONTENT,AclFunction.FTN_AMD_EXAM_MAIN_PERFORMANCE,AclFunction.FTN_AMD_TEACHING_COURSE_LIST};
            return AccessControlWZB.hasRolePrivilege( prof.current_role, ftns);
   
            
   }


    

    
  
 
    
}