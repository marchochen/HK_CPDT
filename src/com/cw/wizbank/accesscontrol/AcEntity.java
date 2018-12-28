package com.cw.wizbank.accesscontrol;

import java.sql.*;
//import com.cw.wizbank.db.view.ViewAcEntity;

/**
Logical Layer Access Control on Entity<BR>
*/
public class AcEntity extends AcInstance {

    /**
    function ext id, approver
    */
    //public static final String FTN_ENT_APPROVER = "ENT_APPROVER";
    
    /**
    rol_ext_id of approver
    */
    //public static final String ROL_APPROVER = "APPR";
    
    public AcEntity(Connection con) {
        super(con);
        view = null; 
    }   
}