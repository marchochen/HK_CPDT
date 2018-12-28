package com.cw.wizbank.db.view;

import java.sql.*;

/**
Database Layer Instance Access Control on Resources<BR>
Access Control Table: acResources<BR>
*/
public class ViewAcResources extends ViewAcInstance {
    
    /**
    ftn_type of this view
    */
    private static final String FtnType = "RESOURCE";
    
    /**
    all the thing need to do in the constructor is to initialize a set of super class variables<BR>
    see ViewAcInstance for details
    */
    public ViewAcResources(Connection con) {
        super(con);
        
        //access control table name
        dbTableName = "acResources";
        
        //instance id column name of the access control table
        colInstanceId = "ac_res_id";

        //entity id column name of the access control table
        colEntityId = "ac_res_ent_id";
        
        //role ext id column name of the access control table
        colRoleExtId = "ac_res_rol_ext_id";
        
        //function ext id column of the access control table
        colFunctionExtId = "ac_res_ftn_ext_Id";
        
        //owner indicator column name of the access control table
        colOwnerInd = "ac_res_owner_ind";
        
        //create user id column name of the access control table
        colCreateUsrId = "ac_res_create_usr_id";
        
        //create timestamp column name of the access control table
        colCreateTimestamp = "ac_res_create_timestamp";
        
        //funtion types of this instance will have
        ftn_types.addElement(FtnType);
    }    
}