package com.cw.wizbank.accesscontrol;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.db.view.ViewAcSurvey;
import com.cwn.wizbank.security.AclFunction;

/**
Logical Layer Access Control on Survey<BR>
*/
public class AcSurvey extends AcModule {

    /**
    function ext id, send survey
    */
   // public static String FTN_SVY_SEND = "SVY_SEND";
    
    /**
    function ext id, submit survey
    */
   // public static String FTN_SVY_SUBMIT = "SVY_SUBMIT";
    
    public AcSurvey(Connection con) {
        super(con);
        view = new ViewAcSurvey(con);
    }
    
  
}