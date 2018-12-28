package com.cw.wizbank.db;

import java.sql.*;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.db.sql.SqlStatements;

/**
A Class represent database table ObjectView
*/
public class DbObjectView {

    //possible values for object view type and subtype
    public static final String OJV_TYPE_LEARNER_REPORT = "LEARNER_REPORT";
    public static final String OJV_TYPE_COURSE_REPORT = "COURSE_REPORT";
    public static final String OJV_TYPE_LEARNING_ACTIVITY_LRN = "LEARNING_ACTIVITY_LRN";
    public static final String OJV_TYPE_LEARNING_ACTIVITY_COS = "LEARNING_ACTIVITY_COS";
    public static final String OJV_TYPE_TARGET_LEARNER_REPORT = "TARGET_LEARNER_REPORT";
    public static final String OJV_TYPE_MODULE = "LEARNING_MODULE";
    public static final String OJV_TYPE_SURVEY_COURSE_REPORT = "SURVEY_COURSE_REPORT";
    public static final String OJV_TYPE_EXAM_PAPER_STAT = "EXAM_PAPER_STAT";
    public static final String OJV_TYPE_TRAIN_FEE_STAT = "TRAIN_FEE_STAT";
    public static final String OJV_TYPE_TRAIN_COST_STAT = "TRAIN_COST_STAT";
    public static final String OJV_TYPE_SURVEY_REPORT = "SURVEY_REPORT";
    public static final String OJV_SUBTYPE_USR = "USR";
    public static final String OJV_SUBTYPE_USR_CLASSIFY = "USR_CLASSIFY";
    public static final String OJV_SUBTYPE_ITM = "ITM";
    public static final String OJV_SUBTYPE_RUN = "RUN";
    public static final String OJV_SUBTYPE_OTHER = "OTHER";

    //Class variables
    private long        ojv_owner_ent_id;
    private String      ojv_type;
    private String      ojv_subtype;
    private String      ojv_option_xml;
    private String      ojv_create_usr_id;
    private Timestamp   ojv_create_timestamp;
    private String      ojv_update_usr_id;
    private Timestamp   ojv_update_timestamp;

    /*
    public void setOwnerEntId(long owner_ent_id) {
        this.ojv_owner_ent_id = owner_ent_id;
    }

    public void setType(String type) {
        this.ojv_type = type;
    }
    
    public void setSubType(String subtype) {
        this.ojv_subtype = subtype;
    }
    */
    
    /**
    Get object view's site id
    */
    public long getOwnerEntId() {
        return this.ojv_owner_ent_id;
    }
    
    /**
    Get object view's type
    */
    public String getType() {
        return this.ojv_type;
    }
    
    /**
    Get object view's subtype
    */
    public String getSubType() {
        return this.ojv_subtype;
    }
    
    /**
    Get object view's option xml
    */
    public String getOptionXML() {
        return this.ojv_option_xml;
    }
    
    /**
    Get object view's create user id
    */
    public String getCreateUsrId() {
        return this.ojv_create_usr_id;
    }

    /**
    Get object view's create timestamp
    */
    public Timestamp getCreateTimestamp() {
        return this.ojv_create_timestamp;
    }

    /**
    Get object view's update user id
    */
    public String getUpdateUsrId() {
        return this.ojv_update_usr_id;
    }

    /**
    Get object view's update timestamp
    */
    public Timestamp getUpdateTimestamp() {
        return this.ojv_update_timestamp;
    }
    
    /**
    Method used to access an object view from database
    @param con Connection to database
    @param owner_ent_id object view's site id
    @param type object view's type
    @param subtype object view'w subtype
    @return an instance of DbObjectView representing the underly record in database,
            or null if no such record is found
    */
    public static DbObjectView getObjectView(Connection con, long owner_ent_id, 
                                             String type, String subtype) 
        throws SQLException {
        
        PreparedStatement stmt = null;
        DbObjectView objectView = null;
        try {
            stmt = con.prepareStatement(SqlStatements.SQL_GET_OJV);
            stmt.setLong(1, owner_ent_id);
            stmt.setString(2, type);
            stmt.setString(3, subtype);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                objectView = new DbObjectView();
                objectView.ojv_owner_ent_id = owner_ent_id;
                objectView.ojv_type = type;
                objectView.ojv_subtype = subtype;
                objectView.ojv_create_usr_id = rs.getString("ojv_create_usr_id");
                objectView.ojv_create_timestamp = rs.getTimestamp("ojv_create_timestamp");
                objectView.ojv_update_usr_id = rs.getString("ojv_update_usr_id");
                objectView.ojv_update_timestamp = rs.getTimestamp("ojv_update_timestamp");
                objectView.ojv_option_xml = cwSQL.getClobValue(rs, "ojv_option_xml");
            } 
        } finally {
            if(stmt!=null) stmt.close();
        }
        return objectView;
    }
    
}

