package com.cw.wizbank.db.view;

import java.sql.*;

/**
Database Layer Instance Access Control on Module<BR>
Access Control Table: acResources<BR>
*/
public class ViewAcSurvey extends ViewAcModule {
    
    /**
    ftn_type of this view
    */
    private static final String FtnType = "SURVEY";
    
    /**
    all the thing need to do in the constructor is to add the ftn_type
    */
    public ViewAcSurvey(Connection con) {
        super(con);
        
       //funtion types of this instance will have
        ftn_types.addElement(FtnType);
    }    
}