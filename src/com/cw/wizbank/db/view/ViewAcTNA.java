package com.cw.wizbank.db.view;

import java.sql.*;

/**
Database Layer Instance Access Control on Module<BR>
Access Control Table: acResources<BR>
*/
public class ViewAcTNA extends ViewAcSurvey {
    
    /**
    ftn_type of this view
    */
    private static final String FtnType = "TNA";
    
    /**
    all the thing need to do in the constructor is to add the ftn_type
    */
    public ViewAcTNA(Connection con) {
        super(con);
        
       //funtion types of this instance will have
        ftn_types.addElement(FtnType);
    }    
}